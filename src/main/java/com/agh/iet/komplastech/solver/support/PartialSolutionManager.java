package com.agh.iet.komplastech.solver.support;

import com.google.common.collect.Streams;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Ints;
import com.google.common.util.concurrent.Striped;
import com.hazelcast.aggregation.Aggregator;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.stream.Collectors;

import static com.agh.iet.komplastech.solver.factories.HazelcastGeneralFactory.GENERAL_FACTORY_ID;
import static com.agh.iet.komplastech.solver.factories.HazelcastGeneralFactory.GET_COLS_FROM_ROW;

public class PartialSolutionManager {

    private final Mesh mesh;
    private final IMap<Integer, double[]> solutionRows;

    private static final Map<Integer, double[]> columnCache = new HashMap<>(4096);
    private static Striped<ReadWriteLock> arrayOfLocks = Striped.readWriteLock(4096);

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

    public double[][] getCols(int... indices) {
        final Iterable<ReadWriteLock> toObtain = arrayOfLocks.bulkGet(Arrays.asList(indices));
        final Set<Lock> readLocks = Streams.stream(toObtain)
                .map(ReadWriteLock::readLock)
                .collect(Collectors.toSet());

        readLocks.forEach(Lock::lock);

        if (areCached(indices)) {
            return getColsFromCache(indices);
        } else {
            final Set<Lock> writeLocks = Streams.stream(toObtain)
                    .map(ReadWriteLock::writeLock)
                    .collect(Collectors.toSet());

            readLocks.forEach(Lock::unlock);

            if (writeLocks.stream().allMatch(Lock::tryLock)) {
                double[][] aggregate = solutionRows.aggregate(new GetColumnsAggregator(indices));
                writeLocks.forEach(Lock::unlock);
                return aggregate;
            } else {
                readLocks.forEach(Lock::lock);
                double[][] colsFromCache = getColsFromCache(indices);
                readLocks.forEach(Lock::unlock);
                return colsFromCache;
            }
        }
    }

    private boolean areCached(int[] indices) {
        return Arrays.stream(indices).boxed().allMatch(columnCache::containsKey);
    }

    private double[][] getColsFromCache(int[] indices) {
        System.out.println("Read from cache: " + Arrays.stream(indices).mapToObj(String::valueOf).collect(Collectors.joining(", ")));
        return Arrays.stream(indices).boxed().map(columnCache::get).toArray(double[][]::new);
    }

    public static void clearCache() {

    }

    public static class GetColumnsAggregator extends Aggregator<Map.Entry<Integer, double[]>, double[][]>
            implements IdentifiedDataSerializable {

        private LinkedList<Integer> rowsRead;
        private Map<Integer, LinkedList<Double>> columns;

        public GetColumnsAggregator() {

        }

        private GetColumnsAggregator(int[] columnsToGet) {
            rowsRead = new LinkedList<>();
            columns = new LinkedHashMap<>(columnsToGet.length);
            Arrays.stream(columnsToGet).forEach(column -> columns.put(column, new LinkedList<>()));
        }

        @Override
        public void accumulate(Map.Entry<Integer, double[]> input) {
            final double[] values = input.getValue();
            columns.forEach((column, cells) -> cells.add(values[column]));
            rowsRead.add(input.getKey());
        }

        @Override
        public void combine(Aggregator aggregator) {
            GetColumnsAggregator other = (GetColumnsAggregator) aggregator;
            columns.forEach((column, cells) -> cells.addAll(other.columns.get(column)));
            rowsRead.addAll(other.rowsRead);
        }

        @Override
        public double[][] aggregate() {
            final double[][] columns = new double[this.columns.size()][rowsRead.size() + 1];
            Iterator<LinkedList<Double>> iterator = this.columns.values().iterator();
            for(int c = 0; c < this.columns.size(); c++) {
                List<Double> unorderedCells = new ArrayList<>(iterator.next());
                columns[c] = Doubles.toArray(rowsRead.stream()
                        .map(r -> unorderedCells.get(r - 1))
                        .collect(Collectors.toList()));
            }
            return columns;
        }

        @Override
        public int getFactoryId() {
            return GENERAL_FACTORY_ID;
        }

        @Override
        public int getId() {
            return GET_COLS_FROM_ROW;
        }

        @Override
        public void writeData(ObjectDataOutput out) throws IOException {
            out.writeInt(columns.size());
            out.writeIntArray(Ints.toArray(rowsRead));
            for (Map.Entry<Integer, LinkedList<Double>> cellsInColumn : columns.entrySet()) {
                out.writeInt(cellsInColumn.getKey());
                out.writeDoubleArray(Doubles.toArray(cellsInColumn.getValue()));
            }
        }

        @Override
        public void readData(ObjectDataInput in) throws IOException {
            final int columnCount = in.readInt();
            rowsRead = Arrays.stream(in.readIntArray()).boxed().collect(Collectors.toCollection(LinkedList::new));
            columns = new LinkedHashMap<>();
            for (int c = 0; c < columnCount; c++) {
                final int column = in.readInt();
                LinkedList<Double> cellsInColumn = new LinkedList<>();
                cellsInColumn.addAll(Doubles.asList(in.readDoubleArray()));
                columns.put(column, cellsInColumn);
            }
        }

    }

}
