package com.agh.iet.komplastech.solver.productions.solution.backsubstitution;

import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.support.Vertex;

public class E2_1_5 extends PFEProduction {

    private final Mesh mesh;

    public E2_1_5(Mesh mesh) {
        this.mesh = mesh;
    }

    public Vertex apply(Vertex T) {
        return partial_forward_elimination(T, 1, 5, mesh.getDofsY());
    }

}