package com.agh.iet.komplastech.solver.productions.solution.factorization;

import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.support.Vertex;
import com.agh.iet.komplastech.solver.productions.Production;

public class A2_3 extends Production {
    public A2_3(Vertex Vert, Mesh Mesh) {
        super(Vert, Mesh);
    }

    public Vertex apply(Vertex T) {
        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 3; j++) {
                T.m_a[i][j] += T.leftChild.m_a[i][j];
                T.m_a[i + 1][j + 1] += T.middleChild.m_a[i][j];
                T.m_a[i + 2][j + 2] += T.rightChild.m_a[i][j];
            }
            for (int j = 1; j <= T.mesh.getDofsY(); j++) {
                T.m_b[i][j] += T.leftChild.m_b[i][j];
                T.m_b[i + 1][j] += T.middleChild.m_b[i][j];
                T.m_b[i + 2][j] += T.rightChild.m_b[i][j];
            }
        }
        // bring 3rd degree of freedom to the front
        swapDofs(1, 3, 5, T.mesh.getDofsY());
        swapDofs(2, 3, 5, T.mesh.getDofsY());
        return T;
    }
}