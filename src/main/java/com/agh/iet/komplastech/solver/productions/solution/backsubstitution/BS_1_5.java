package com.agh.iet.komplastech.solver.productions.solution.backsubstitution;

import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.support.Vertex;

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