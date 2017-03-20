package com.agh.iet.komplastech.solver.initialization;

import com.agh.iet.komplastech.solver.ProductionExecutorFactory;
import com.agh.iet.komplastech.solver.logger.SolutionLogger;
import com.agh.iet.komplastech.solver.problem.Problem;
import com.agh.iet.komplastech.solver.productions.Production;
import com.agh.iet.komplastech.solver.productions.initialization.A;
import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.tracking.VerticalIterator;

public class HorizontalLeafInitializer implements LeafInitializer {

    private final Mesh mesh;

    private final Problem rhs;

    private final ProductionExecutorFactory launcherFactory;

    private final SolutionLogger solutionLogger;

    public HorizontalLeafInitializer(Mesh mesh, Problem rhs, ProductionExecutorFactory launcherFactory, SolutionLogger solutionLogger) {
        this.mesh = mesh;
        this.rhs = rhs;
        this.launcherFactory = launcherFactory;
        this.solutionLogger = solutionLogger;
    }

    @Override
    public void initializeLeaves(VerticalIterator leafLevelVertices) {
        final Production production = new A(mesh, rhs);
        leafLevelVertices.forEachGoingUpOnce((range) -> {
            launcherFactory
                    .launchProduction(production)
                    .inVertexRange(range)
                    .andWaitTillComplete();
            solutionLogger.logMatrixValuesFor(range, "Initialize leaves");
        });
    }


}
