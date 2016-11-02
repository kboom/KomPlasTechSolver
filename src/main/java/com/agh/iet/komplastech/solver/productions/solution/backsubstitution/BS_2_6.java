package com.agh.iet.komplastech.solver.productions.solution.backsubstitution;

import com.agh.iet.komplastech.solver.Mesh;
import com.agh.iet.komplastech.solver.Vertex;

public class BS_2_6 extends PFEProduction {
    public BS_2_6(Vertex Vert, Mesh Mesh) {
        super(Vert, Mesh);
    }

    public Vertex apply(Vertex T) {
        System.out.println("BS_2_6");
        T = partial_backward_substitution(T, 2, 6, m_mesh.getDofsY());
        swapDofs(1, 3, 6, T.m_mesh.getDofsY());
        swapDofs(2, 4, 6, T.m_mesh.getDofsY());
        for (int i = 1; i <= 4; i++) {
            for (int j = 1; j <= T.m_mesh.getDofsY(); j++) {
                T.leftChild.m_x[i + 1][j] = T.m_x[i][j];
                T.rightChild.m_x[i + 1][j] = T.m_x[i + 2][j];
            }
        }

        return T;
    }
}