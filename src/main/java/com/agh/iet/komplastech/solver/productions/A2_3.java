package com.agh.iet.komplastech.solver.productions;

import com.agh.iet.komplastech.solver.MeshData;
import com.agh.iet.komplastech.solver.Vertex;

import java.util.concurrent.CyclicBarrier;

public class A2_3 extends Production {
    public A2_3(Vertex Vert, MeshData Mesh) {
        super(Vert, Mesh);
    }

    Vertex apply(Vertex T) {
        System.out.println("A2_3");
        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 3; j++) {
                T.m_a[i][j] += T.m_left.m_a[i][j];
                T.m_a[i + 1][j + 1] += T.m_middle.m_a[i][j];
                T.m_a[i + 2][j + 2] += T.m_right.m_a[i][j];
            }
            for (int j = 1; j <= T.m_mesh.m_dofsy; j++) {
                T.m_b[i][j] += T.m_left.m_b[i][j];
                T.m_b[i + 1][j] += T.m_middle.m_b[i][j];
                T.m_b[i + 2][j] += T.m_right.m_b[i][j];
            }
        }
        // bring 3rd degree of freedom to the front
        swapDofs(1, 3, 5, T.m_mesh.m_dofsy);
        swapDofs(2, 3, 5, T.m_mesh.m_dofsy);
        return T;
    }
}