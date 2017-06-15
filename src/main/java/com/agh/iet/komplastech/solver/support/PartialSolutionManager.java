package com.agh.iet.komplastech.solver.support;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.map.EntryBackupProcessor;
import com.hazelcast.map.EntryProcessor;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class PartialSolutionManager {

    private final Mesh mesh;
    private final IMap<Integer, double[]> solutionRows;

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
    }

    public double[][] getCols(int... indices) {
        double[][] cols = new double[indices.length][mesh.getElementsX() + mesh.getSplineOrder() + 1];

        solutionRows.executeOnEntries(new EntryProcessor<Integer, double[]>() {

            @Override
            public double[] process(Map.Entry<Integer, double[]> entry) {
                return Arrays.stream(indices).mapToDouble(col -> entry.getValue()[col]).toArray();
            }

            @Override
            public EntryBackupProcessor<Integer, double[]> getBackupProcessor() {
                return null;
            }

        }).forEach((row, value) -> {
            double[] values = (double[]) value;
            for (int col = 0; col < indices.length; col++) {
                cols[col][row] = values[col];
            }
        });

        return cols;
    }

}
