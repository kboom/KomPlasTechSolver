package com.agh.iet.komplastech.solver;

import java.util.concurrent.CyclicBarrier;

class A2_2 extends Production {
    A2_2(Vertex Vert, CyclicBarrier Barrier, MeshData Mesh) {
        super(Vert, Barrier, Mesh);
    }

    Vertex apply(Vertex T) {
        System.out.println("A2_2");
        for (int i = 1; i <= 4; i++) {
            for (int j = 1; j <= 4; j++) {
                T.m_a[i][j] += T.m_left.m_a[i + 1][j + 1];
                T.m_a[i + 2][j + 2] += T.m_right.m_a[i + 1][j + 1];
            }
            for (int j = 1; j <= T.m_mesh.m_dofsy; j++) {
                T.m_b[i][j] += T.m_left.m_b[i + 1][j];
                T.m_b[i + 2][j] += T.m_right.m_b[i + 1][j];
            }
        }
        // exchange 1st,2nd and 3rd,4rd rows and columns
        swapDofs(1, 3, 6, T.m_mesh.m_dofsy);
        swapDofs(2, 4, 6, T.m_mesh.m_dofsy);
        return T;
    }
}
