package com.agh.iet.komplastech.solver.productions.solution.factorization;

import com.agh.iet.komplastech.solver.productions.Production;
import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.support.Vertex;

import static com.agh.iet.komplastech.solver.productions.VertexUtils.swapDofsFor;

public class A2_3 implements Production {

    private final Mesh mesh;

    public A2_3(Mesh mesh) {
        this.mesh = mesh;
    }

    public Vertex apply(Vertex T) {
        final Vertex leftChild = T.getLeftChild();
        final Vertex middleChild = T.getMiddleChild();
        final Vertex rightChild = T.getRightChild();

        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 3; j++) {
                T.m_a[i][j] += leftChild.m_a[i][j];
                T.m_a[i + 1][j + 1] += middleChild.m_a[i][j];
                T.m_a[i + 2][j + 2] += rightChild.m_a[i][j];
            }
            for (int j = 1; j <= mesh.getDofsY(); j++) {
                T.m_b[i][j] += leftChild.m_b[i][j];
                T.m_b[i + 1][j] += middleChild.m_b[i][j];
                T.m_b[i + 2][j] += rightChild.m_b[i][j];
            }
        }
        // bring 3rd degree of freedom to the front
        swapDofsFor(T, 1, 3, 5, mesh.getDofsY());
        swapDofsFor(T, 2, 3, 5, mesh.getDofsY());
        return T;
    }

}