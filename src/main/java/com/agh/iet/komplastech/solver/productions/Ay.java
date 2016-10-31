package com.agh.iet.komplastech.solver.productions;

import com.agh.iet.komplastech.solver.MeshData;
import com.agh.iet.komplastech.solver.Vertex;

import java.util.concurrent.CyclicBarrier;

public class Ay extends Production {
    public Ay(Vertex Vert, double[][] solution, double[] partition, int idx, MeshData Mesh) {
        super(Vert, Mesh);
        this.solution = solution;
        this.partition = partition;
        this.idx = idx;
    }

    Vertex apply(Vertex T) {
        System.out.println("Ay");
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
        for (int i = 1; i <= T.m_mesh.m_dofsx; i++) {
            T.m_b[1][i] = partition[0] * solution[i][idx + 1];
            T.m_b[2][i] = partition[1] * solution[i][idx + 2];
            T.m_b[3][i] = partition[2] * solution[i][idx + 3];
        }
        return T;
    }

    double[][] solution;
    double[] partition;
    int idx;
}
