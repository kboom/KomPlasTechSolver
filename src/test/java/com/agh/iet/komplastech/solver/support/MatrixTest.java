package com.agh.iet.komplastech.solver.support;


import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MatrixTest {

    @Test
    public void canGetValues() {
        Matrix matrix = createDummyMatrix();
        assertThat(matrix.get(0,0)).isEqualTo(1);
        assertThat(matrix.get(0,1)).isEqualTo(2);
        assertThat(matrix.get(0,2)).isEqualTo(3);
        assertThat(matrix.get(1,0)).isEqualTo(4);
        assertThat(matrix.get(1,1)).isEqualTo(5);
        assertThat(matrix.get(1,2)).isEqualTo(6);
    }

    @Test
    public void canDivideValues() {
        Matrix matrix = createDummyMatrix();
        matrix.divideBy(1, 1, 5);
        assertThat(matrix.to2DArray()).isEqualTo(new double[][] { new double[] {1, 2, 3}, new double[] {4, 1, 6}});
    }

    @Test
    public void canSubtractValues() {
        Matrix matrix = createDummyMatrix();
        matrix.subtract(0, 2, 4);
        assertThat(matrix.to2DArray()).isEqualTo(new double[][] { new double[] {1, 2, -1}, new double[] {4, 5, 6}});
    }

    @Test
    public void canAddValues() {
        Matrix matrix = createDummyMatrix();
        matrix.add(0, 2, 100);
        assertThat(matrix.to2DArray()).isEqualTo(new double[][] { new double[] {1, 2, 103}, new double[] {4, 5, 6}});
    }

    @Test
    public void canSwapValues() {
        Matrix matrix = createDummyMatrix();
        matrix.swap(0, 0, 1, 2);
        assertThat(matrix.to2DArray()).isEqualTo(new double[][] { new double[] {6, 2, 3}, new double[] {4, 5, 1}});
    }

    @Test
    public void canConvertTo2DArray() {
        Matrix matrix = createDummyMatrix();
        assertThat(matrix.to2DArray()).isEqualTo(new double[][] { new double[] {1, 2, 3}, new double[] {4, 5, 6}});
    }

    @Test
    public void canGetRow() {
        Matrix matrix = createDummyMatrix();
        assertThat(matrix.getRow(0)).isEqualTo(new double[] {1, 2, 3});
        assertThat(matrix.getRow(1)).isEqualTo(new double[] {4, 5, 6});
    }

    private Matrix createDummyMatrix() {
        Matrix matrix = new Matrix(2,3);
        matrix.set(0,0,1);
        matrix.set(0,1,2);
        matrix.set(0,2,3);
        matrix.set(1,0,4);
        matrix.set(1,1,5);
        matrix.set(1,2,6);
        return matrix;
    }

}