package com.agh.iet.komplastech.solver;

/**
 * @(#)P1.java
 *
 *
 * @author 
 * @version 1.00 2015/2/23
 */
import java.util.concurrent.CyclicBarrier;

class P1 extends Production {
    P1(Vertex Vert, CyclicBarrier Barrier, MeshData Mesh) {
        super(Vert, Barrier, Mesh);
    }

    Vertex apply(Vertex S) {
        System.out.println("p1");
        S.m_left = new Vertex(null, null, null, S, "T", 0, S.m_mesh.m_nelemx / 2, S.m_mesh);
        S.m_right = new Vertex(null, null, null, S, "T", S.m_mesh.m_nelemx / 2, S.m_mesh.m_nelemx, S.m_mesh);
        S.set_label("root x");
        return S;
    }
}
