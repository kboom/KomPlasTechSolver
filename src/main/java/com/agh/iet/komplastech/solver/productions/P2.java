package com.agh.iet.komplastech.solver.productions;

import com.agh.iet.komplastech.solver.MeshData;
import com.agh.iet.komplastech.solver.Vertex;

import java.util.concurrent.CyclicBarrier;

public class P2 extends Production {
    public P2(Vertex Vert, CyclicBarrier Barrier, MeshData Mesh) {
        super(Vert, Barrier, Mesh);
    }

    Vertex apply(Vertex T) {
        System.out.println("p2");
        Vertex T1 = new Vertex(null, null, null, T, "T", T.m_beg, T.m_beg + (T.m_end - T.m_beg) * 0.5, T.m_mesh);
        Vertex T2 = new Vertex(null, null, null, T, "T", T.m_beg + (T.m_end - T.m_beg) * 0.5, T.m_end, T.m_mesh);
        T.set_left(T1);
        T.set_right(T2);
        T.set_label("int");
        return T;
    }
}
