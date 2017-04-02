package com.agh.iet.komplastech.solver.support;

import java.io.Serializable;
import java.util.Arrays;

public class Matrix implements Serializable {

    private double[] elements;
    private int rows;
    private int cols;

    public Matrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.elements = new double[rows * cols];
    }

//    @Override
//    public void writeData(ObjectDataOutput out) throws IOException {
//        out.writeInt(rows);
//        out.writeInt(cols);
//        out.writeObject(elements);
//    }
//
//    @Override
//    public void readData(ObjectDataInput in) throws IOException {
//        rows = in.readInt();
//        cols = in.readInt();
//        elements = in.readObject();
//    }

    public double get(int row, int col) {
        return elements[rows * row + col];
    }

    public void set(int row, int col, double value) {
        elements[rows * row + col] = value;
    }

    public void swap(int rowA, int colA, int rowB, int colB) {
        final int indexA = rows * rowA + colA;
        final int indexB = rows * rowB + colB;
        double tmp = elements[indexA];
        elements[indexA] = elements[indexB];
        elements[indexB] = tmp;
    }

    public void add(int row, int col, double value) {
        final int index = rows * row + col;
        elements[index] += value;
    }

    public void divideBy(int row, int col, double divisor) {
        final int index = rows * row + col;
        elements[index] /= divisor;
    }

    public void subtract(int row, int col, double value) {
        final int index = rows * row + col;
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
}
