package com.agh.iet.komplastech.solver.productions.initialization;

import com.agh.iet.komplastech.solver.RightHandSide;
import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.support.Vertex;

import static com.agh.iet.komplastech.solver.productions.initialization.SampleCoefficients.useArbitraryCoefficients;

public class A1 extends A {
    public A1(Vertex Vert, Mesh Mesh, RightHandSide rhs) {
        super(Vert, Mesh, rhs);
    }

//    public Vertex apply(Vertex node) {
//        initializeCoefficientsMatrix(node);
//        fillRightHandSides(node);
//        return node;
//    }
//
//    private void initializeCoefficientsMatrix(Vertex node) {
//        useArbitraryCoefficients(node);
//    }
//
//    private void fillRightHandSides(Vertex node) {
//        for (int i = 1; i <= node.mesh.getDofsY(); i++) {
//            node.m_b[1][i] = 1.0;
//            node.m_b[2][i] = 1.0;
//            node.m_b[3][i] = 1.0;
//        }
//    }

}
