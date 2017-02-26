package com.agh.iet.komplastech.solver.productions.solution.factorization;

import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.support.Vertex;
import com.agh.iet.komplastech.solver.productions.Production;

public class Aroot extends Production {

    public Aroot(Mesh mesh) {
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
        return T;
    }

}


