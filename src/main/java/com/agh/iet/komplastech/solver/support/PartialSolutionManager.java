package com.agh.iet.komplastech.solver.support;

import com.agh.iet.komplastech.solver.factories.HazelcastGeneralFactory.GeneralObjectType;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Ints;
import com.hazelcast.aggregation.Aggregator;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.agh.iet.komplastech.solver.factories.HazelcastGeneralFactory.GENERAL_FACTORY_ID;

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
        double[][] array = new double[rows][columns]; // todo this probably was the reason for failure - huge memory pressure during the initialisation of the next step - done for each (x,y)...!!!
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
        return solutionRows.aggregate(new GetColumnsAggregator(indices));
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
            for (int c = 0; c < this.columns.size(); c++) {
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
            return GeneralObjectType.GET_COLS_FROM_ROW.id;
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
