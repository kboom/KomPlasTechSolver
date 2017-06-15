package com.agh.iet.komplastech.solver.support;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class PartialSolutionManager {

    private final IMap<Integer, double[]> solutionRows;

    public PartialSolutionManager(HazelcastInstance hazelcastInstance) {
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

}
