package com.agh.iet.komplastech.solver.productions.solution.backsubstitution;

import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.support.Vertex;

public class Eroot extends PFEProduction {

    private final Mesh mesh;

    public Eroot(Mesh mesh) {
        this.mesh = mesh;
    }

    public Vertex apply(Vertex T) {
        final Vertex leftChild = T.getLeftChild();
        final Vertex rightChild = T.getRightChild();

        T = partial_forward_elimination(T, 6, 6, mesh.getDofsY());
        T = partial_backward_substitution(T, 6, 6, mesh.getDofsY());

        for (int i = 1; i <= 4; i++) {
            for (int j = 1; j <= mesh.getDofsY(); j++) {
                leftChild.m_x[i + 2][j] = T.m_x[i][j];
                rightChild.m_x[i + 2][j] = T.m_x[i + 2][j];
            }
        }

        return T;
    }

}