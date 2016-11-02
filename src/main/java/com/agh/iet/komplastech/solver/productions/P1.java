package com.agh.iet.komplastech.solver.productions;
/**
 * @(#)P1.java
 *
 *
 * @author 
 * @version 1.00 2015/2/23
 */
import com.agh.iet.komplastech.solver.Mesh;
import com.agh.iet.komplastech.solver.Vertex;

public class P1 extends Production {
    public P1(Vertex Vert, Mesh Mesh) {
        super(Vert, Mesh);
    }

    Vertex apply(Vertex S) {
        System.out.println("p1");
        S.m_left = new Vertex(null, null, null, 0, S.m_mesh.getElementsX() / 2, S.m_mesh);
        S.m_right = new Vertex(null, null, null, S.m_mesh.getElementsX() / 2, S.m_mesh.getElementsX(), S.m_mesh);
        return S;
    }
}
