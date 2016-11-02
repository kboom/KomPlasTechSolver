package com.agh.iet.komplastech.solver.productions.solution;

import com.agh.iet.komplastech.solver.Mesh;
import com.agh.iet.komplastech.solver.Vertex;

public class BS_1_5 extends PFEProduction {

    public BS_1_5(Vertex Vert, Mesh Mesh) {
        super(Vert, Mesh);
    }

    public Vertex apply(Vertex T) {
        System.out.println("BS_1_5");
        T = partial_backward_substitution(T, 1, 5, m_mesh.getDofsY());
        swapDofs(1, 2, 5, m_mesh.getDofsY());
        swapDofs(2, 3, 5, m_mesh.getDofsY());
        return T;
    }
}