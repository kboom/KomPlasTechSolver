package com.agh.iet.komplastech.solver.support;


import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MatrixTest {

    @Test
    public void canGetValues() {
        Matrix matrix = createDummyMatrix();
        assertThat(matrix.get(0,0)).isEqualTo(1);
        assertThat(matrix.get(0,1)).isEqualTo(2);
        assertThat(matrix.get(1,0)).isEqualTo(3);
        assertThat(matrix.get(1,1)).isEqualTo(4);
    }

    @Test
    public void canDivideValues() {
        Matrix matrix = createDummyMatrix();
        matrix.divideBy(1, 0, 3);
        assertThat(matrix.to2DArray()).isEqualTo(new double[][] { new double[] {1, 2}, new double[] {1, 4}});
    }

    @Test
    public void canSubtractValues() {
        Matrix matrix = createDummyMatrix();
        matrix.subtract(1, 0, 4);
        assertThat(matrix.to2DArray()).isEqualTo(new double[][] { new double[] {1, 2}, new double[] {-1, 4}});
    }

    @Test
    public void canAddValues() {
        Matrix matrix = createDummyMatrix();
        matrix.add(0, 1, 100);
        assertThat(matrix.to2DArray()).isEqualTo(new double[][] { new double[] {1, 102}, new double[] {3, 4}});
    }

    @Test
    public void canSwapValues() {
        Matrix matrix = createDummyMatrix();
        matrix.swap(0, 0, 1, 1);
        assertThat(matrix.to2DArray()).isEqualTo(new double[][] { new double[] {4, 2}, new double[] {3, 1}});
    }

    @Test
    public void canConvertTo2DArray() {
        Matrix matrix = createDummyMatrix();
        assertThat(matrix.to2DArray()).isEqualTo(new double[][] { new double[] {1, 2}, new double[] {3, 4}});
    }

    @Test
    public void canGetRow() {
        Matrix matrix = createDummyMatrix();
        assertThat(matrix.getRow(0)).isEqualTo(new double[] {1, 2});
        assertThat(matrix.getRow(1)).isEqualTo(new double[] {3, 4});
    }

    private Matrix createDummyMatrix() {
        Matrix matrix = new Matrix(2,2);
        matrix.set(0,0,1);
        matrix.set(0,1,2);
        matrix.set(1,0,3);
        matrix.set(1,1,4);
        return matrix;
    }

}