package com.agh.iet.komplastech.solver.productions;

import com.agh.iet.komplastech.solver.Solution;
import com.agh.iet.komplastech.solver.productions.construction.P1y;
import com.agh.iet.komplastech.solver.support.Mesh;

public class VerticalProductionFactory extends HorizontalProductionFactory {

    private Mesh mesh;

    public VerticalProductionFactory(
            Mesh mesh,
            Solution horizontalSolution) {
        super(mesh, horizontalSolution.getProblem());
        this.mesh = mesh;
    }

    @Override
    public Production createBranchRootProduction() {
        return new P1y(mesh);
    }

}
