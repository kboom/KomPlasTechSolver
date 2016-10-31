package com.agh.iet.komplastech.solver.productions;

import com.agh.iet.komplastech.solver.Mesh;
import com.agh.iet.komplastech.solver.Vertex;

public class E2_2_6 extends PFEProduction {
    public E2_2_6(Vertex Vert, Mesh Mesh) {
        super(Vert, Mesh);
    }

    Vertex apply(Vertex T) {
        System.out.println("E2_2_6");
        T = partial_forward_elimination(T, 2, 6, m_mesh.getDofsY());
        return T;
    }
}