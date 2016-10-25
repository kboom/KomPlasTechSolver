package com.agh.iet.komplastech.solver.productions;

import com.agh.iet.komplastech.solver.MeshData;
import com.agh.iet.komplastech.solver.Vertex;
import com.agh.iet.komplastech.solver.productions.PFEProduction;

import java.util.concurrent.CyclicBarrier;

public class Eroot extends PFEProduction {
    public Eroot(Vertex Vert, CyclicBarrier Barrier, MeshData Mesh) {
        super(Vert, Barrier, Mesh);
    }

    Vertex apply(Vertex T) {
        System.out.println("Eroot");
        T = partial_forward_elimination(T, 6, 6, m_mesh.m_dofsy);
        T = partial_backward_substitution(T, 6, 6, m_mesh.m_dofsy);

        for (int i = 1; i <= 4; i++) {
            for (int j = 1; j <= T.m_mesh.m_dofsy; j++) {
                T.m_left.m_x[i + 2][j] = T.m_x[i][j];
                T.m_right.m_x[i + 2][j] = T.m_x[i + 2][j];
            }
        }

        return T;
    }
}