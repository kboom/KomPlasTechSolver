package com.agh.iet.komplastech.solver.productions.initialization;

import com.agh.iet.komplastech.solver.Mesh;
import com.agh.iet.komplastech.solver.Vertex;

public class A1 extends A {
    public A1(Vertex Vert, Mesh Mesh) {
        super(Vert, Mesh);
    }

    public Vertex apply(Vertex node) {
        System.out.println("A1");
        node.m_a[1][1] = 1.0 / 20.0;
        node.m_a[1][2] = 13.0 / 120;
        node.m_a[1][3] = 1.0 / 120;
        node.m_a[2][1] = 13.0 / 120.0;
        node.m_a[2][2] = 45.0 / 100.0;
        node.m_a[2][3] = 13.0 / 120.0;
        node.m_a[3][1] = 1.0 / 120.0;
        node.m_a[3][2] = 13.0 / 120.0;
        node.m_a[3][3] = 1.0 / 20.0;
        // multiple right-hand sides
        for (int i = 1; i <= node.m_mesh.getDofsY(); i++) {
            node.m_b[1][i] = 1.0;
            node.m_b[2][i] = 1.0;
            node.m_b[3][i] = 1.0;
        }
        return node;
    }

}
