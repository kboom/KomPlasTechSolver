package com.agh.iet.komplastech.solver.support;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;

import static java.lang.System.arraycopy;

public class Matrix implements DataSerializable {

    private double[] elements;
    private int rows;
    private int cols;

    @SuppressWarnings("unused")
    public Matrix() {

    }

    public Matrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.elements = new double[rows * cols];
    }

    public Matrix(double[][] values) {
        this.rows = values.length;
        this.cols = values[0].length;
        this.elements = new double[rows * cols];
        for(int row = 0; row < rows; row++) {
            arraycopy(values[row], 0, elements, cols * row, cols);
        }
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeInt(rows);
        out.writeInt(cols);
        out.writeDoubleArray(elements);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        rows = in.readInt();
        cols = in.readInt();
        elements = in.readDoubleArray();
    }

    public double get(int row, int col) {
        return elements[cols * row + col];
    }

    public void set(int row, int col, double value) {
        elements[cols * row + col] = value;
    }

    public void swap(int rowA, int colA, int rowB, int colB) {
        final int indexA = cols * rowA + colA;
        final int indexB = cols * rowB + colB;
        double tmp = elements[indexA];
        elements[indexA] = elements[indexB];
        elements[indexB] = tmp;
    }

    public void add(int row, int col, double value) {
        final int index = cols * row + col;
        elements[index] += value;
    }

    public void divideBy(int row, int col, double divisor) {
        final int index = cols * row + col;
        elements[index] /= divisor;
    }

    public void subtract(int row, int col, double value) {
        final int index = cols * row + col;
        elements[index] -= value;
    }

    public double[] getRow(int row) {
        return Arrays.copyOfRange(elements, row * cols, row * cols + cols);
    }

    public double[][] to2DArray() {
        double[][] array = new double[rows][cols];
        for (int row = 0; row < rows; row++) {
            array[row] = Arrays.copyOfRange(elements, row * cols, row * cols + cols);
        }
        return array;
    }

    public static Matrix from2DArray(double[][] rhs) {
        return new Matrix(rhs);
    }

}
