package com.agh.iet.komplastech.solver;

import java.util.concurrent.CyclicBarrier;

class BS_2_6 extends PFEProduction {
    BS_2_6(Vertex Vert, CyclicBarrier Barrier, MeshData Mesh) {
        super(Vert, Barrier, Mesh);
    }

    Vertex apply(Vertex T) {
        System.out.println("BS_2_6");
        T = partial_backward_substitution(T, 2, 6, m_mesh.m_dofsy);
        swapDofs(1, 3, 6, T.m_mesh.m_dofsy);
        swapDofs(2, 4, 6, T.m_mesh.m_dofsy);
        for (int i = 1; i <= 4; i++) {
            for (int j = 1; j <= T.m_mesh.m_dofsy; j++) {
                T.m_left.m_x[i + 1][j] = T.m_x[i][j];
                T.m_right.m_x[i + 1][j] = T.m_x[i + 2][j];
            }
        }

        return T;
    }
}