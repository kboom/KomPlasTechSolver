package com.agh.iet.komplastech.solver.productions.solution.factorization;

import com.agh.iet.komplastech.solver.productions.Production;
import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.support.Vertex;

import static com.agh.iet.komplastech.solver.productions.VertexUtils.swapDofsFor;

public class A2_2 extends Production {

    public A2_2(Mesh Mesh) {
        super(Mesh);
    }

    public Vertex apply(Vertex T) {
        for (int i = 1; i <= 4; i++) {
            for (int j = 1; j <= 4; j++) {
                T.m_a[i][j] += T.leftChild.m_a[i + 1][j + 1];
                T.m_a[i + 2][j + 2] += T.rightChild.m_a[i + 1][j + 1];
            }
            for (int j = 1; j <= T.mesh.getDofsY(); j++) {
                T.m_b[i][j] += T.leftChild.m_b[i + 1][j];
                T.m_b[i + 2][j] += T.rightChild.m_b[i + 1][j];
            }
        }
        swapDofsFor(T, 1, 3, 6, T.mesh.getDofsY());
        swapDofsFor(T, 2, 4, 6, T.mesh.getDofsY());
        return T;
    }

}
