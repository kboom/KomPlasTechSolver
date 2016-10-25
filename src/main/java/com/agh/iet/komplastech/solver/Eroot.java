package com.agh.iet.komplastech.solver;

import java.util.concurrent.CyclicBarrier;

class Eroot extends PFEProduction {
    Eroot(Vertex Vert, CyclicBarrier Barrier, MeshData Mesh) {
        super(Vert, Barrier, Mesh);
    }

    Vertex apply(Vertex T) {
        System.out.println("Eroot");
        T = partial_forward_elimination(T, 6, 6, m_mesh.m_dofsy);
        T = partial_backward_substitution(T, 6, 6, m_mesh.m_dofsy);

        for (int i = 1; i <= 4; i++) {
            for (int j = 1; j <= T.m_mesh.m_dofsy; j++) {
                T.m_left.m_x[i + 2][j] = T.m_x[i][j];
                T.m_right.m_x[i + 2][j] = T.m_x[i + 2][j];
            }
        }

        return T;
    }
}