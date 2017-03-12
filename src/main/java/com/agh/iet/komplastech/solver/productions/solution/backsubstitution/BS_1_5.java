package com.agh.iet.komplastech.solver.productions.solution.backsubstitution;

import com.agh.iet.komplastech.solver.productions.ProcessingContext;
import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.support.Vertex;

import static com.agh.iet.komplastech.solver.productions.VertexUtils.swapDofsFor;

public class BS_1_5 extends PFEProduction {

    private final Mesh mesh;

    public BS_1_5(Mesh mesh) {
        this.mesh = mesh;
    }

    public Vertex apply(ProcessingContext processingContext) {
        Vertex T = processingContext.getVertex();
        T = partial_backward_substitution(T, 1, 5, mesh.getDofsY());
        swapDofsFor(T, 1, 2, 5, mesh.getDofsY());
        swapDofsFor(T, 2, 3, 5, mesh.getDofsY());
        return T;
    }

}