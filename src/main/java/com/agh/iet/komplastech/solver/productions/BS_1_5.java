package com.agh.iet.komplastech.solver.productions;

import com.agh.iet.komplastech.solver.MeshData;
import com.agh.iet.komplastech.solver.Vertex;

import java.util.concurrent.CyclicBarrier;

public class BS_1_5 extends PFEProduction {

    public BS_1_5(Vertex Vert, CyclicBarrier Barrier, MeshData Mesh) {
        super(Vert, Barrier, Mesh);
    }

    Vertex apply(Vertex T) {
        System.out.println("BS_1_5");
        T = partial_backward_substitution(T, 1, 5, m_mesh.m_dofsy);
        swapDofs(1, 2, 5, m_mesh.m_dofsy);
        swapDofs(2, 3, 5, m_mesh.m_dofsy);
        return T;
    }
}