package com.agh.iet.komplastech.solver.productions;
/**
 * @(#)P1.java
 *
 *
 * @author 
 * @version 1.00 2015/2/23
 */
import com.agh.iet.komplastech.solver.MeshData;
import com.agh.iet.komplastech.solver.Vertex;
import com.agh.iet.komplastech.solver.productions.Production;

import java.util.concurrent.CyclicBarrier;

public class P1y extends Production {
    public P1y(Vertex Vert, MeshData Mesh) {
        super(Vert, Mesh);
    }

    Vertex apply(Vertex S) {
        System.out.println("p1y");
        S.m_left = new Vertex(null, null, null, S, "T", 0, S.m_mesh.m_nelemy / 2.0, S.m_mesh);
        S.m_right = new Vertex(null, null, null, S, "T", S.m_mesh.m_nelemy / 2.0, S.m_mesh.m_nelemy, S.m_mesh);
        S.set_label("root y");
        return S;
    }
}
