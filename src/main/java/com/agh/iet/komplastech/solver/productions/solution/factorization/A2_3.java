package com.agh.iet.komplastech.solver.productions.solution.factorization;

import com.agh.iet.komplastech.solver.productions.ProcessingContext;
import com.agh.iet.komplastech.solver.productions.Production;
import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.support.Vertex;

import static com.agh.iet.komplastech.solver.productions.VertexUtils.swapDofsFor;

public class A2_3 implements Production {

    private final Mesh mesh;

    public A2_3(Mesh mesh) {
        this.mesh = mesh;
    }

    public Vertex apply(ProcessingContext processingContext) {
        final Vertex currentVertex = processingContext.getVertex();

        final Vertex leftChild = currentVertex.getLeftChild();
        final Vertex middleChild = currentVertex.getMiddleChild();
        final Vertex rightChild = currentVertex.getRightChild();

        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 3; j++) {
                currentVertex.m_a[i][j] += leftChild.m_a[i][j];
                currentVertex.m_a[i + 1][j + 1] += middleChild.m_a[i][j];
                currentVertex.m_a[i + 2][j + 2] += rightChild.m_a[i][j];
            }
            for (int j = 1; j <= mesh.getDofsY(); j++) {
                currentVertex.m_b[i][j] += leftChild.m_b[i][j];
                currentVertex.m_b[i + 1][j] += middleChild.m_b[i][j];
                currentVertex.m_b[i + 2][j] += rightChild.m_b[i][j];
            }
        }
        // bring 3rd degree of freedom to the front
        swapDofsFor(currentVertex, 1, 3, 5, mesh.getDofsY());
        swapDofsFor(currentVertex, 2, 3, 5, mesh.getDofsY());
        return currentVertex;
    }

}