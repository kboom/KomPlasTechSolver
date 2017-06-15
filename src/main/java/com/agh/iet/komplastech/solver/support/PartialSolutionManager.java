package com.agh.iet.komplastech.solver.support;

import com.agh.iet.komplastech.solver.factories.HazelcastGeneralFactory;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.map.EntryBackupProcessor;
import com.hazelcast.map.EntryProcessor;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.stream.Collectors;

import static com.agh.iet.komplastech.solver.factories.HazelcastGeneralFactory.GENERAL_FACTORY_ID;

public class PartialSolutionManager {

    private final Mesh mesh;
    private final IMap<Integer, double[]> solutionRows;

    private static final Map<Integer, double[]> columnCache = new WeakHashMap<>(1000);

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
        if(Arrays.stream(indices).boxed().allMatch(columnCache::containsKey)) {
            return Arrays.stream(indices).boxed().map(columnCache::get).toArray(double[][]::new);
        } else {
            double[][] cols = new double[indices.length][mesh.getElementsX() + mesh.getSplineOrder() + 1];
            solutionRows.executeOnEntries(new GetColsFromRow(indices)).forEach((row, value) -> {
                double[] values = (double[]) value;
                for (int col = 0; col < indices.length; col++) {
                    cols[col][row] = values[col];
                }
            });

            for(int i = 0; i < indices.length; i++) {
                columnCache.put(indices[i], cols[i]);
            }

            return cols;
        }
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
