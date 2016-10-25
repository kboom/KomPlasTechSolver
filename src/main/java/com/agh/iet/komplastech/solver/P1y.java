package com.agh.iet.komplastech.solver;

/**
 * @(#)P1.java
 *
 *
 * @author 
 * @version 1.00 2015/2/23
 */
import java.util.concurrent.CyclicBarrier;

class P1y extends Production {
    P1y(Vertex Vert, CyclicBarrier Barrier, MeshData Mesh) {
        super(Vert, Barrier, Mesh);
    }

    Vertex apply(Vertex S) {
        System.out.println("p1y");
        S.m_left = new Vertex(null, null, null, S, "T", 0, S.m_mesh.m_nelemy / 2.0, S.m_mesh);
        S.m_right = new Vertex(null, null, null, S, "T", S.m_mesh.m_nelemy / 2.0, S.m_mesh.m_nelemy, S.m_mesh);
        S.set_label("root y");
        return S;
    }
}
