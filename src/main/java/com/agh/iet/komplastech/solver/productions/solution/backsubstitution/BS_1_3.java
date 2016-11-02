package com.agh.iet.komplastech.solver.productions.solution.backsubstitution;

import com.agh.iet.komplastech.solver.Mesh;
import com.agh.iet.komplastech.solver.Vertex;

public class BS_1_3 extends PFEProduction {
    BS_1_3(Vertex Vert, Mesh Mesh) {
        super(Vert, Mesh);
    }

    public Vertex apply(Vertex T) {
        System.out.println("BS_3_3");
        T = partial_backward_substitution(T, 1, 3, m_mesh.getDofsY());
        return T;
    }
}