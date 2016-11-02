package com.agh.iet.komplastech.solver.productions;

import com.agh.iet.komplastech.solver.Mesh;
import com.agh.iet.komplastech.solver.Vertex;

public class P2 extends Production {
    public P2(Vertex Vert, Mesh Mesh) {
        super(Vert, Mesh);
    }

    Vertex apply(Vertex T) {
        System.out.println("p2");
        Vertex T1 = new Vertex(null, null, null, T.m_beg, T.m_beg + (T.m_end - T.m_beg) * 0.5, T.m_mesh);
        Vertex T2 = new Vertex(null, null, null, T.m_beg + (T.m_end - T.m_beg) * 0.5, T.m_end, T.m_mesh);
        T.setLeftChild(T1);
        T.setRightChild(T2);
        return T;
    }
}
