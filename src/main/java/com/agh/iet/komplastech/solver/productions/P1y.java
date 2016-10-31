package com.agh.iet.komplastech.solver.productions;

import com.agh.iet.komplastech.solver.Mesh;
import com.agh.iet.komplastech.solver.Vertex;

public class P1y extends Production {
    public P1y(Vertex Vert, Mesh Mesh) {
        super(Vert, Mesh);
    }

    Vertex apply(Vertex S) {
        System.out.println("p1y");
        S.m_left = new Vertex(null, null, null, S, "T", 0, S.m_mesh.getElementsY() / 2.0, S.m_mesh);
        S.m_right = new Vertex(null, null, null, S, "T", S.m_mesh.getElementsY() / 2.0, S.m_mesh.getElementsY(), S.m_mesh);
        S.set_label("root y");
        return S;
    }
}
