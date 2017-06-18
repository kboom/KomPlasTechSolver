package com.agh.iet.komplastech.solver.support;

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
import static com.agh.iet.komplastech.solver.factories.HazelcastGeneralFactory.GET_COLS_FROM_ROW;

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

    // no worth in caching rows... only 2 reads of 1 row.
    public double[][] getCols(int... indices) {
        return solutionRows.aggregate(new GetColumnsAggregator(indices));
    }

    public static void clearCache() {

    }

    public static class GetColumnsAggregator extends Aggregator<Map.Entry<Integer, double[]>, double[][]>
            implements IdentifiedDataSerializable {

        private LinkedList<Integer> rowsRead;
        private Map<Integer, LinkedList<Double>> cells;

        public GetColumnsAggregator() {

        }

        private GetColumnsAggregator(int[] columnsToGet) {
            rowsRead = new LinkedList<>();
            cells = new LinkedHashMap<>(columnsToGet.length);
            Arrays.stream(columnsToGet).forEach(column -> cells.put(column, new LinkedList<>()));
        }

        @Override
        public void accumulate(Map.Entry<Integer, double[]> input) {
            System.out.println("ACCUMULATE " + input.getKey() + " at object " + this);
            final double[] values = input.getValue();
            cells.forEach((column, cells) -> cells.add(values[column]));
            rowsRead.add(input.getKey());
        }

        @Override
        public void combine(Aggregator aggregator) {
            GetColumnsAggregator other = (GetColumnsAggregator) aggregator;
            System.out.println(String.format("COMBINE (%s) coming from (%s) - size (%d) with (%s) - size (%d) coming from (%s)"
                    , rowsRead.stream().map(String::valueOf).collect(Collectors.joining(", ")), this, rowsRead.size(), other.rowsRead.stream().map(String::valueOf).collect(Collectors.joining(", ")), other.rowsRead.size(), other));
            rowsRead.addAll(other.rowsRead);
            cells.forEach((column, cells) -> cells.addAll(other.cells.get(column))); // todo order? isn't it doing ABC - FED rather than ABC - DEF?
            System.out.println("NOW COMBINED into "
                    + rowsRead.stream().map(String::valueOf).collect(Collectors.joining(", ")));
        }

        /**
         * This is the final call, called only once, at the end.
         */
        @Override
        public double[][] aggregate() {
            final double[][] columns = new double[cells.size()][rowsRead.size()];
            Iterator<LinkedList<Double>> iterator = cells.values().iterator();
            for(int c = 0; c < cells.size(); c++) {
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
            out.writeInt(cells.size());
            out.writeIntArray(Ints.toArray(rowsRead));
            for (Map.Entry<Integer, LinkedList<Double>> cellsInColumn : cells.entrySet()) {
                out.writeInt(cellsInColumn.getKey());
                out.writeDoubleArray(Doubles.toArray(cellsInColumn.getValue()));
            }
        }

        @Override
        public void readData(ObjectDataInput in) throws IOException {
            final int columnCount = in.readInt();
            rowsRead = Arrays.stream(in.readIntArray()).boxed().collect(Collectors.toCollection(LinkedList::new));
            cells = new LinkedHashMap<>();
            for (int c = 0; c < columnCount; c++) {
                final int column = in.readInt();
                LinkedList<Double> cellsInColumn = new LinkedList<>();
                cellsInColumn.addAll(Doubles.asList(in.readDoubleArray()));
                cells.put(column, cellsInColumn);
            }
        }

    }

}
