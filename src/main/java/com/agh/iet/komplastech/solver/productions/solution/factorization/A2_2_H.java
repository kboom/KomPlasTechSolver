package com.agh.iet.komplastech.solver.productions.solution.factorization;

import com.agh.iet.komplastech.solver.productions.ProcessingContext;
import com.agh.iet.komplastech.solver.productions.Production;
import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.support.Vertex;

import static com.agh.iet.komplastech.solver.productions.VertexUtils.swapDofsFor;

public class A2_2_H implements Production {

    private final Mesh mesh;

    public A2_2_H(Mesh mesh) {
        this.mesh = mesh;
    }

    public Vertex apply(ProcessingContext context) {
        final Vertex currentVertex = context.getVertex();
        final Vertex leftChild = currentVertex.getLeftChild();
        final Vertex rightChild = currentVertex.getRightChild();

        for (int i = 1; i <= 4; i++) {
            for (int j = 1; j <= 4; j++) {
                currentVertex.m_a[i][j] += leftChild.m_a[i + 2][j + 2];
                currentVertex.m_a[i + 2][j + 2] += rightChild.m_a[i + 2][j + 2];
            }
            for (int j = 1; j <= mesh.getDofsY(); j++) {
                currentVertex.m_b[i][j] += leftChild.m_b[i + 2][j];
                currentVertex.m_b[i + 2][j] += rightChild.m_b[i + 2][j];
            }
        }

        swapDofsFor(currentVertex, 1, 3, 6, mesh.getDofsY());
        swapDofsFor(currentVertex, 2, 4, 6, mesh.getDofsY());

        return currentVertex;
    }

}
