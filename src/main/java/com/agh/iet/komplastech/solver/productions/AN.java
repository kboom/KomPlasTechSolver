package com.agh.iet.komplastech.solver.productions;

import com.agh.iet.komplastech.solver.MeshData;
import com.agh.iet.komplastech.solver.Vertex;

import java.util.concurrent.CyclicBarrier;

public class AN extends A {
    public AN(Vertex Vert, MeshData Mesh) {
        super(Vert, Mesh);
    }
/*
    Vertex apply(Vertex T) {
        System.out.println("AN");
        T.m_a[1][1] = 1.0 / 20.0;
        T.m_a[1][2] = 13.0 / 120;
        T.m_a[1][3] = 1.0 / 120;
        T.m_a[2][1] = 13.0 / 120.0;
        T.m_a[2][2] = 45.0 / 100.0;
        T.m_a[2][3] = 13.0 / 120.0;
        T.m_a[3][1] = 1.0 / 120.0;
        T.m_a[3][2] = 13.0 / 120.0;
        T.m_a[3][3] = 1.0 / 20.0;
        // multiple right-hand sides
        for (int i = 1; i <= T.m_mesh.m_dofsy; i++) {
            T.m_b[1][i] = 1.0;
            T.m_b[2][i] = 1.0;
            T.m_b[3][i] = 1.0;
        }
        return T;
    }
*/    
}
