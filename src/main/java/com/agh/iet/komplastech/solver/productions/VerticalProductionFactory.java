package com.agh.iet.komplastech.solver.productions;

import com.agh.iet.komplastech.solver.Solution;
import com.agh.iet.komplastech.solver.problem.Problem;
import com.agh.iet.komplastech.solver.productions.construction.P1y;
import com.agh.iet.komplastech.solver.storage.ObjectStore;
import com.agh.iet.komplastech.solver.support.Mesh;

public class VerticalProductionFactory extends HorizontalProductionFactory {

    private final Solution horizontalSolution;
    private Mesh mesh;

    public VerticalProductionFactory(
            ObjectStore objectStore,
            Mesh mesh,
            Solution horizontalSolution) {
        super(objectStore, mesh, horizontalSolution.getProblem());
        this.mesh = mesh;
        this.horizontalSolution = horizontalSolution;
    }

    @Override
    public Production createRootProduction() {
        return new P1y(mesh);
    }

}
