package com.agh.iet.komplastech.solver.initialization;

import com.agh.iet.komplastech.solver.ProductionExecutorFactory;
import com.agh.iet.komplastech.solver.problem.Problem;
import com.agh.iet.komplastech.solver.productions.Production;
import com.agh.iet.komplastech.solver.productions.initialization.A;
import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.tracking.VerticalIterator;

public class HorizontalLeafInitializer implements LeafInitializer {

    private final Mesh mesh;

    private final Problem rhs;

    private final ProductionExecutorFactory launcherFactory;

    public HorizontalLeafInitializer(Mesh mesh, Problem rhs, ProductionExecutorFactory launcherFactory) {
        this.mesh = mesh;
        this.rhs = rhs;
        this.launcherFactory = launcherFactory;
    }

    @Override
    public void initializeLeaves(VerticalIterator leafLevelVertices) {
        final Production production = new A(mesh, rhs);
        leafLevelVertices.forEachStayingAt((vertices) -> launcherFactory
                .launchProduction(production)
                .onVertices(vertices)
                .andWaitTillComplete());
    }


}
