package com.agh.iet.komplastech.solver;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * @(#)Production.java
 *
 *
 * @author
 * @version 1.00 2015/2/23
 */

abstract class Production extends Thread {
    Production(Vertex Vert, CyclicBarrier Barrier, MeshData Mesh) {
        m_vertex = Vert;
        m_barrier = Barrier;
        m_mesh = Mesh;
    }

    // returns first vertex from the left
    abstract Vertex apply(Vertex v);

    // run the thread
    public void run() {
        // apply the production
        m_vertex = apply(m_vertex);
        try {
            m_barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

    // vertex where the production will be applied
    Vertex m_vertex;
    // productions counter
    CyclicBarrier m_barrier;
    // mesh data
    MeshData m_mesh;

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