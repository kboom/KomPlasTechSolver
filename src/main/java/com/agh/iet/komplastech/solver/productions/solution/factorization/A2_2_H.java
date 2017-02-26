package com.agh.iet.komplastech.solver.productions.solution.factorization;

import com.agh.iet.komplastech.solver.productions.Production;
import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.support.Vertex;

import static com.agh.iet.komplastech.solver.productions.VertexUtils.swapDofsFor;

public class A2_2_H extends Production {

    public A2_2_H(Mesh mesh) {
        super(mesh);
    }

    public Vertex apply(Vertex T) {
        final Vertex leftChild = T.getLeftChild();
        final Vertex rightChild = T.getRightChild();

        for (int i = 1; i <= 4; i++) {
            for (int j = 1; j <= 4; j++) {
                T.m_a[i][j] += leftChild.m_a[i + 2][j + 2];
                T.m_a[i + 2][j + 2] += rightChild.m_a[i + 2][j + 2];
            }
            for (int j = 1; j <= mesh.getDofsY(); j++) {
                T.m_b[i][j] += leftChild.m_b[i + 2][j];
                T.m_b[i + 2][j] += rightChild.m_b[i + 2][j];
            }
        }

        swapDofsFor(T, 1, 3, 6, mesh.getDofsY());
        swapDofsFor(T, 2, 4, 6, mesh.getDofsY());

        return T;
    }

}
