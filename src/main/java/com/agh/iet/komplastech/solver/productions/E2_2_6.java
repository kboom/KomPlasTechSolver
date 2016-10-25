package com.agh.iet.komplastech.solver.productions;

import com.agh.iet.komplastech.solver.MeshData;
import com.agh.iet.komplastech.solver.Vertex;

import java.util.concurrent.CyclicBarrier;

public class E2_2_6 extends PFEProduction {
    public E2_2_6(Vertex Vert, CyclicBarrier Barrier, MeshData Mesh) {
        super(Vert, Barrier, Mesh);
    }

    Vertex apply(Vertex T) {
        System.out.println("E2_2_6");
        T = partial_forward_elimination(T, 2, 6, m_mesh.m_dofsy);
        return T;
    }
}