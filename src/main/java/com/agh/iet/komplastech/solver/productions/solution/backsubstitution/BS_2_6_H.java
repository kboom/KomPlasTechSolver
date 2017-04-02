package com.agh.iet.komplastech.solver.productions.solution.backsubstitution;

import com.agh.iet.komplastech.solver.productions.ProcessingContext;
import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.support.Vertex;

import static com.agh.iet.komplastech.solver.productions.VertexUtils.swapDofsFor;

public class BS_2_6_H extends PFEProduction {

    private final Mesh mesh;

    public BS_2_6_H(Mesh mesh) {
        this.mesh = mesh;
    }

    public void apply(ProcessingContext processingContext) {
        Vertex T = processingContext.getVertex();

        final Vertex leftChild = T.getLeftChild();
        final Vertex rightChild = T.getRightChild();

        T = partial_backward_substitution(T, 2, 6, mesh.getDofsY());
        swapDofsFor(T, 1, 3, 6, mesh.getDofsY());
        swapDofsFor(T, 2, 4, 6, mesh.getDofsY());

        for (int i = 1; i <= 4; i++) {
            for (int j = 1; j <= mesh.getDofsY(); j++) {
                leftChild.m_x[i + 2][j] = T.m_x[i][j];
                rightChild.m_x[i + 2][j] = T.m_x[i + 2][j];
            }
        }

        processingContext.updateVertexAndChildren();
    }

}
