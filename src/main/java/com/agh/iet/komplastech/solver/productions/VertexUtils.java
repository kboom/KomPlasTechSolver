package com.agh.iet.komplastech.solver.productions;

import com.agh.iet.komplastech.solver.support.Vertex;

public class VertexUtils {

    public static void swapDofsFor(Vertex vertex, int a, int b, int size, int nrhs) {
        for (int i = 1; i <= size; i++) {
            double temp = vertex.m_a[a][i];
            vertex.m_a[a][i] = vertex.m_a[b][i];
            vertex.m_a[b][i] = temp;
        }
        for (int i = 1; i <= size; i++) {
            double temp = vertex.m_a[i][a];
            vertex.m_a[i][a] = vertex.m_a[i][b];
            vertex.m_a[i][b] = temp;
        }
        for (int i = 1; i <= nrhs; i++) {
            double temp = vertex.m_b[a][i];
            vertex.m_b[a][i] = vertex.m_b[b][i];
            vertex.m_b[b][i] = temp;

            temp = vertex.m_x[a][i];
            vertex.m_x[a][i] = vertex.m_x[b][i];
            vertex.m_x[b][i] = temp;
        }
    }

}
