package com.agh.iet.komplastech.solver.productions.solution.backsubstitution;

import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.support.Vertex;

public class E2_1_5 extends PFEProduction {
    public E2_1_5(Vertex Vert, Mesh Mesh) {
        super(Vert, Mesh);
    }

    public Vertex apply(Vertex T) {
        System.out.println("E2_1_5");
        T = partial_forward_elimination(T, 1, 5, m_mesh.getDofsY());
        return T;
    }
}