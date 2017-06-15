package com.agh.iet.komplastech.solver.support;

import com.agh.iet.komplastech.solver.factories.HazelcastGeneralFactory;
import com.google.common.collect.Streams;
import com.google.common.util.concurrent.Striped;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.map.EntryBackupProcessor;
import com.hazelcast.map.EntryProcessor;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.stream.Collectors;

import static com.agh.iet.komplastech.solver.factories.HazelcastGeneralFactory.GENERAL_FACTORY_ID;

public class PartialSolutionManager {

    private final Mesh mesh;
    private final IMap<Integer, double[]> solutionRows;

    private static final Map<Integer, double[]> columnCache = new HashMap<>(1024);
    private static Striped<ReadWriteLock> arrayOfLocks = Striped.readWriteLock(1024);

    public PartialSolutionManager(Mesh mesh, HazelcastInstance hazelcastInstance) {
        this.mesh = mesh;
        this.solutionRows = hazelcastInstance.getMap("solution");
    }

    public double[][] getRows(int... indices) {
        Map<Integer, double[]> all = solutionRows.getAll(Arrays.stream(indices).boxed().collect(Collectors.toSet()));
        int columns = all.get(indices[0]).length;
        int rows = indices.length;
        double[][] array = new double[rows][columns];
        int minIndex = Arrays.stream(indices).min().getAsInt();
        all.entrySet().parallelStream().forEach((entry) -> array[entry.getKey() - minIndex] = entry.getValue());
        return array;
    }

    public void setAll(Map<Integer, double[]> pairs) {
        solutionRows.putAll(pairs);
    }

    public void clear() {
        solutionRows.clear();
        columnCache.clear();
    }

    // todo probably should read more than required at once? How to read the smallest number of times without sacrificing parallelism?
    public double[][] getCols(int... indices) {
        Iterable<ReadWriteLock> toObtain = arrayOfLocks.bulkGet(Arrays.asList(indices));

        System.out.println("Read Locking on " + Arrays.stream(indices).mapToObj(String::valueOf).collect(Collectors.joining(", ")));

        Set<Lock> readLocks = Streams.stream(toObtain)
                .map(ReadWriteLock::readLock)
                .collect(Collectors.toSet());

        readLocks.forEach(Lock::lock);

        if (areCached(indices)) {
            return getColsFromCache(indices);
        } else {

            System.out.println("Write locking on " + Arrays.stream(indices).mapToObj(String::valueOf).collect(Collectors.joining(", ")));
            Set<Lock> writeLocks = Streams.stream(toObtain)
                    .map(ReadWriteLock::writeLock)
                    .collect(Collectors.toSet());


            readLocks.forEach(Lock::unlock); // must release read lock before acquiring write lock

            if (writeLocks.stream().allMatch(Lock::tryLock)) {
                double[][] cols = new double[indices.length][mesh.getElementsX() + mesh.getSplineOrder() + 1];
                solutionRows.executeOnEntries(new GetColsFromRow(indices)).forEach((row, value) -> {
                    double[] values = (double[]) value;
                    for (int col = 0; col < indices.length; col++) {
                        cols[col][row] = values[col];
                    }
                });

                for (int i = 0; i < indices.length; i++) {
                    System.out.println("Put in cache: " + Arrays.stream(indices).mapToObj(String::valueOf).collect(Collectors.joining(", ")));
                    columnCache.put(indices[i], cols[i]);
                }

                writeLocks.forEach(Lock::unlock);

                return cols;
            } else {
                readLocks.forEach(Lock::lock);
                double[][] colsFromCache = getColsFromCache(indices);
                readLocks.forEach(Lock::unlock);
                return colsFromCache;
            }
        }

    }

    private double[][] getColsFromCache(int[] indices) {
        System.out.println("Read from cache: " + Arrays.stream(indices).mapToObj(String::valueOf).collect(Collectors.joining(", ")));
        return Arrays.stream(indices).boxed().map(columnCache::get).toArray(double[][]::new);
    }

    private boolean areCached(int[] indices) {
        return Arrays.stream(indices).boxed().allMatch(columnCache::containsKey);
    }

    public static void clearCache() {
        columnCache.clear();
    }

    public static class GetColsFromRow implements IdentifiedDataSerializable, EntryProcessor<Integer, double[]> {

        private int[] indices;

        GetColsFromRow(int[] indices) {
            this.indices = indices;
        }

        public GetColsFromRow() {

        }

        @Override
        public double[] process(Map.Entry<Integer, double[]> entry) {
            return Arrays.stream(indices).mapToDouble(col -> entry.getValue()[col]).toArray();
        }

        @Override
        public EntryBackupProcessor<Integer, double[]> getBackupProcessor() {
            return null;
        }

        @Override
        public int getFactoryId() {
            return GENERAL_FACTORY_ID;
        }

        @Override
        public int getId() {
            return HazelcastGeneralFactory.GET_COLS_FROM_ROW;
        }

        @Override
        public void writeData(ObjectDataOutput out) throws IOException {
            out.writeIntArray(indices);
        }

        @Override
        public void readData(ObjectDataInput in) throws IOException {
            indices = in.readIntArray();
        }

    }

}
