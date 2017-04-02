package com.agh.iet.komplastech.solver.productions;

import com.agh.iet.komplastech.solver.support.Vertex;

public class VertexUtils {

    public static void swapDofsFor(Vertex vertex, int a, int b, int size, int nrhs) {
        for (int i = 1; i <= size; i++) {
            vertex.m_a.swap(a, i, b, i);
        }

        for (int i = 1; i <= size; i++) {
            vertex.m_a.swap(i, a, i, b);
        }

        for (int i = 1; i <= nrhs; i++) {
            vertex.m_b.swap(a, i, b, i);
            vertex.m_x.swap(a, i, b, i);
        }
    }

}
