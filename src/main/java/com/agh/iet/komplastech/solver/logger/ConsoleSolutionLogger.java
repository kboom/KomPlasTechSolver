package com.agh.iet.komplastech.solver.logger;

import com.agh.iet.komplastech.solver.VertexId;
import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.support.Vertex;
import com.agh.iet.komplastech.solver.support.VertexMap;
import com.agh.iet.komplastech.solver.support.VertexRange;

import static java.lang.String.format;

public class ConsoleSolutionLogger implements SolutionLogger {

    private final Mesh mesh;
    private final VertexMap vertexMap;

    public ConsoleSolutionLogger(Mesh mesh, VertexMap vertexMap) {
        this.mesh = mesh;
        this.vertexMap = vertexMap;
    }

    @Override
    public void logMatrixValuesFor(VertexRange vertexRange, String message) {
        System.out.println(format("------------------------------- %s START -------------------------------", message));
        vertexMap.getAllInRange(vertexRange).forEach(
                vertex -> {
                    printMatrix(vertex, 6, mesh.getDofsY());
                    System.out.println("----------------------------");
                }
        );
        System.out.println(format("------------------------------- %s END -------------------------------", message));
    }

    @Override
    public void logMatrixValuesAt(VertexId vertex, String message) {

    }

    private static void printMatrix(final Vertex v, int size, int nrhs) {
        for (int i = 1; i <= size; ++i) {
            for (int j = 1; j <= size; ++j) {
                System.out.printf("%6.3f ", v.m_a.get(i, j));
            }
            System.out.printf("  |  ");
            for (int j = 1; j <= nrhs; ++j) {
                System.out.printf("%6.3f ", v.m_b.get(i, j));
            }
            System.out.printf("  |  ");
            for (int j = 1; j <= nrhs; ++j) {
                System.out.printf("%6.3f ", v.m_x.get(i, j));
            }
            System.out.println();
        }
    }

}
