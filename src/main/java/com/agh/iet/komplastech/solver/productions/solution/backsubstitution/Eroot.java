package com.agh.iet.komplastech.solver.productions.solution.backsubstitution;

import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.support.Vertex;

public class Eroot extends PFEProduction {
    public Eroot(Vertex Vert, Mesh Mesh) {
        super(Vert, Mesh);
    }

    public Vertex apply(Vertex T) {
        System.out.println("Eroot");
        T = partial_forward_elimination(T, 6, 6, m_mesh.getDofsY());
        T = partial_backward_substitution(T, 6, 6, m_mesh.getDofsY());

        for (int i = 1; i <= 4; i++) {
            for (int j = 1; j <= T.mesh.getDofsY(); j++) {
                T.leftChild.m_x[i + 2][j] = T.m_x[i][j];
                T.rightChild.m_x[i + 2][j] = T.m_x[i + 2][j];
            }
        }

        return T;
    }

}