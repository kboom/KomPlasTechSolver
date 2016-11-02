package com.agh.iet.komplastech.solver.productions;

import com.agh.iet.komplastech.solver.Mesh;
import com.agh.iet.komplastech.solver.Vertex;

public class A2_2 extends Production {
    public A2_2(Vertex Vert, Mesh Mesh) {
        super(Vert, Mesh);
    }

    public Vertex apply(Vertex T) {
        System.out.println("A2_2");
        for (int i = 1; i <= 4; i++) {
            for (int j = 1; j <= 4; j++) {
                T.m_a[i][j] += T.m_left.m_a[i + 1][j + 1];
                T.m_a[i + 2][j + 2] += T.m_right.m_a[i + 1][j + 1];
            }
            for (int j = 1; j <= T.m_mesh.getDofsY(); j++) {
                T.m_b[i][j] += T.m_left.m_b[i + 1][j];
                T.m_b[i + 2][j] += T.m_right.m_b[i + 1][j];
            }
        }
        // exchange 1st,2nd and 3rd,4rd rows and columns
        swapDofs(1, 3, 6, T.m_mesh.getDofsY());
        swapDofs(2, 4, 6, T.m_mesh.getDofsY());
        return T;
    }
}
