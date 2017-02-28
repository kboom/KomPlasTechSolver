package com.agh.iet.komplastech.solver.productions.solution.backsubstitution;

import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.support.Vertex;

import static com.agh.iet.komplastech.solver.productions.VertexUtils.swapDofsFor;

public class BS_2_6 extends PFEProduction {

    private final Mesh mesh;

    public BS_2_6(Mesh mesh) {
        this.mesh = mesh;
    }

    /**
     * todo: CHILD modifying operation!!! Store the result!
     * @param T
     * @return
     */
    public Vertex apply(Vertex T) {
        final Vertex leftChild = T.getLeftChild();
        final Vertex rightChild = T.getRightChild();

        T = partial_backward_substitution(T, 2, 6, mesh.getDofsY());
        swapDofsFor(T, 1, 3, 6, mesh.getDofsY());
        swapDofsFor(T, 2, 4, 6, mesh.getDofsY());
        for (int i = 1; i <= 4; i++) {
            for (int j = 1; j <= mesh.getDofsY(); j++) {
                leftChild.m_x[i + 1][j] = T.m_x[i][j];
                rightChild.m_x[i + 1][j] = T.m_x[i + 2][j];
            }
        }

        return T;
    }

}