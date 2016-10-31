package com.agh.iet.komplastech.solver.productions;

import com.agh.iet.komplastech.solver.MeshData;
import com.agh.iet.komplastech.solver.Vertex;

import java.util.concurrent.CyclicBarrier;

public class P3 extends Production {
    public P3(Vertex Vert, MeshData Mesh) {
        super(Vert, Mesh);
    }

    Vertex apply(Vertex T) {
        System.out.println("p3");
        Vertex T1 = new Vertex(null, null, null, T, "T", T.m_beg, T.m_beg + (T.m_end - T.m_beg) / 3.0, T.m_mesh);
        Vertex T2 = new Vertex(null, null, null, T, "T", T.m_beg + (T.m_end - T.m_beg) / 3.0,
                T.m_end - (T.m_end - T.m_beg) / 3.0, T.m_mesh);
        Vertex T3 = new Vertex(null, null, null, T, "T", T.m_beg + (T.m_end - T.m_beg) * 2.0 / 3.0, T.m_end, T.m_mesh);
        T.set_left(T1);
        T.set_middle(T2);
        T.set_right(T3);
        T.set_label("leaf");
        return T;
    }
}