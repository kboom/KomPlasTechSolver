package com.agh.iet.komplastech.solver.productions.solution.backsubstitution;

import com.agh.iet.komplastech.solver.productions.ProcessingContext;
import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.support.Vertex;

public class Eroot extends PFEProduction {

    private final Mesh mesh;

    public Eroot(Mesh mesh) {
        this.mesh = mesh;
    }

    public void apply(ProcessingContext processingContext) {
        Vertex T = processingContext.getVertex();

        final Vertex leftChild = T.getLeftChild();
        final Vertex rightChild = T.getRightChild();

        T = partial_forward_elimination(T, 6, 6, mesh.getDofsY());
        T = partial_backward_substitution(T, 6, 6, mesh.getDofsY());

        for (int i = 1; i <= 4; i++) {
            for (int j = 1; j <= mesh.getDofsY(); j++) {
                leftChild.m_x.set(i + 2, j, T.m_x.get(i, j));
                rightChild.m_x.set(i + 2, j, T.m_x.get(i + 2, j));
            }
        }

        processingContext.updateVertexAndChildren();
    }

}