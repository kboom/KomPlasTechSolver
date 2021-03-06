package com.agh.iet.komplastech.solver.results;

import com.agh.iet.komplastech.solver.support.Vertex;

public class MatrixPrinter {

    public static void printMatrix(final Vertex v, int size, int nrhs) {
        for (int i = 1; i <= size; ++i) {
            for (int j = 1; j <= size; ++j) {
                System.out.printf("%6.3f ", v.m_a[i][j]);
            }
            System.out.printf("  |  ");
            for (int j = 1; j <= nrhs; ++j) {
                System.out.printf("%6.3f ", v.m_b[i][j]);
            }
            System.out.printf("  |  ");
            for (int j = 1; j <= nrhs; ++j) {
                System.out.printf("%6.3f ", v.m_x[i][j]);
            }
            System.out.println();
        }
    }

}
