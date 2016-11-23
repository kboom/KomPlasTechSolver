package com.agh.iet.komplastech.solver.productions.solution.backsubstitution;

import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.support.Vertex;

public class E2_2_6 extends PFEProduction {
    public E2_2_6(Vertex Vert, Mesh Mesh) {
        super(Vert, Mesh);
    }

    public Vertex apply(Vertex T) {
        System.out.println("E2_2_6");
        T = partial_forward_elimination(T, 2, 6, m_mesh.getDofsY());
        return T;
    }
}