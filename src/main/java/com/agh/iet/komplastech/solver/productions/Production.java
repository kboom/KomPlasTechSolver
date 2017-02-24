package com.agh.iet.komplastech.solver.productions;

import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.support.Vertex;

import java.io.Serializable;
import java.util.concurrent.CyclicBarrier;


public abstract class Production implements Serializable {

    public Production(Vertex Vert, Mesh Mesh) {
        m_vertex = Vert;
        m_mesh = Mesh;
    }

    // returns first vertex from the left
    public abstract Vertex apply(Vertex v);

    public void run() {
        m_vertex = apply(m_vertex);
    }

    public Vertex m_vertex;
    public Mesh m_mesh;

    protected void swapDofs(int a, int b, int size, int nrhs) {
        for (int i = 1; i <= size; i++) {
            double temp = m_vertex.m_a[a][i];
            m_vertex.m_a[a][i] = m_vertex.m_a[b][i];
            m_vertex.m_a[b][i] = temp;
        }
        for (int i = 1; i <= size; i++) {
            double temp = m_vertex.m_a[i][a];
            m_vertex.m_a[i][a] = m_vertex.m_a[i][b];
            m_vertex.m_a[i][b] = temp;
        }
        for (int i = 1; i <= nrhs; i++) {
            double temp = m_vertex.m_b[a][i];
            m_vertex.m_b[a][i] = m_vertex.m_b[b][i];
            m_vertex.m_b[b][i] = temp;

            temp = m_vertex.m_x[a][i];
            m_vertex.m_x[a][i] = m_vertex.m_x[b][i];
            m_vertex.m_x[b][i] = temp;
        }
    }
}