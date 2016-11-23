package com.agh.iet.komplastech.solver.productions.solution.backsubstitution;

import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.support.Vertex;

public class BS_1_3 extends PFEProduction {
    BS_1_3(Vertex Vert, Mesh Mesh) {
        super(Vert, Mesh);
    }

    public Vertex apply(Vertex T) {
        T = partial_backward_substitution(T, 1, 3, m_mesh.getDofsY());
        return T;
    }
}