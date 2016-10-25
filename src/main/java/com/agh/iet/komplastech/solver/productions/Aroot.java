package com.agh.iet.komplastech.solver.productions;

import com.agh.iet.komplastech.solver.MeshData;
import com.agh.iet.komplastech.solver.Vertex;

import java.util.concurrent.CyclicBarrier;

public class Aroot extends Production {
    public Aroot(Vertex Vert, CyclicBarrier Barrier, MeshData Mesh) {
        super(Vert, Barrier, Mesh);
    }

    Vertex apply(Vertex T) {
        System.out.println("Aroot");
        for (int i = 1; i <= 4; i++) {
            for (int j = 1; j <= 4; j++) {
                T.m_a[i][j] += T.m_left.m_a[i + 2][j + 2];
                T.m_a[i + 2][j + 2] += T.m_right.m_a[i + 2][j + 2];
            }
            for (int j = 1; j <= T.m_mesh.m_dofsy; j++) {
                T.m_b[i][j] += T.m_left.m_b[i + 2][j];
                T.m_b[i + 2][j] += T.m_right.m_b[i + 2][j];
            }
        }
        return T;
    }
}
