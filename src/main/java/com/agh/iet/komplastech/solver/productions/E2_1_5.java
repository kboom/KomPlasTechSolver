package com.agh.iet.komplastech.solver.productions;

import com.agh.iet.komplastech.solver.MeshData;
import com.agh.iet.komplastech.solver.Vertex;
import com.agh.iet.komplastech.solver.productions.PFEProduction;

import java.util.concurrent.CyclicBarrier;

public class E2_1_5 extends PFEProduction {
    public E2_1_5(Vertex Vert, CyclicBarrier Barrier, MeshData Mesh) {
        super(Vert, Barrier, Mesh);
    }

    Vertex apply(Vertex T) {
        System.out.println("E2_1_5");
        T = partial_forward_elimination(T, 1, 5, m_mesh.m_dofsy);
        return T;
    }
}