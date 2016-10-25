package com.agh.iet.komplastech.solver;

import java.util.concurrent.CyclicBarrier;

class BS_1_3 extends PFEProduction {
    BS_1_3(Vertex Vert, CyclicBarrier Barrier, MeshData Mesh) {
        super(Vert, Barrier, Mesh);
    }

    Vertex apply(Vertex T) {
        System.out.println("BS_3_3");
        T = partial_backward_substitution(T, 1, 3, m_mesh.m_dofsy);
        return T;
    }
}