package com.agh.iet.komplastech.solver.productions.solution.factorization;

import com.agh.iet.komplastech.solver.productions.Production;
import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.support.Vertex;

// apply A2_3, then A2_2 but then this one (A2_2_H) up to root (root -> Aroot)
public class A2_2_H extends Production {

    public A2_2_H(Vertex Vert, Mesh Mesh) {
        super(Vert, Mesh);
    }

    public Vertex apply(Vertex T) {
        System.out.println("Aroot");
        for (int i = 1; i <= 4; i++) {
            for (int j = 1; j <= 4; j++) {
                T.m_a[i][j] += T.leftChild.m_a[i + 2][j + 2];
                T.m_a[i + 2][j + 2] += T.rightChild.m_a[i + 2][j + 2];
            }
            for (int j = 1; j <= T.mesh.getDofsY(); j++) {
                T.m_b[i][j] += T.leftChild.m_b[i + 2][j];
                T.m_b[i + 2][j] += T.rightChild.m_b[i + 2][j];
            }
        }

        swapDofs(1, 3, 6, T.mesh.getDofsY());
        swapDofs(2, 4, 6, T.mesh.getDofsY());

        return T;
    }

}
