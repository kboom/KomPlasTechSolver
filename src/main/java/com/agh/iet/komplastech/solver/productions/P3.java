package com.agh.iet.komplastech.solver.productions;

import com.agh.iet.komplastech.solver.Mesh;
import com.agh.iet.komplastech.solver.Vertex;

public class P3 extends Production {
    public P3(Vertex Vert, Mesh Mesh) {
        super(Vert, Mesh);
    }

    Vertex apply(Vertex T) {
        System.out.println("p3");
        Vertex T1 = new Vertex(null, null, null, T.m_beg, T.m_beg + (T.m_end - T.m_beg) / 3.0, T.m_mesh);
        Vertex T2 = new Vertex(null, null, null, T.m_beg + (T.m_end - T.m_beg) / 3.0,
                T.m_end - (T.m_end - T.m_beg) / 3.0, T.m_mesh);
        Vertex T3 = new Vertex(null, null, null, T.m_beg + (T.m_end - T.m_beg) * 2.0 / 3.0, T.m_end, T.m_mesh);
        T.setLeftChild(T1);
        T.setMiddleChild(T2);
        T.setRightChild(T3);
        return T;
    }
}