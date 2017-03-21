package com.agh.iet.komplastech.solver.initialization;

import com.agh.iet.komplastech.solver.ProductionExecutorFactory;
import com.agh.iet.komplastech.solver.Solution;
import com.agh.iet.komplastech.solver.logger.SolutionLogger;
import com.agh.iet.komplastech.solver.productions.initialization.Ay;
import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.tracking.VerticalIterator;

public class VerticalLeafInitializer implements LeafInitializer {

    private final Mesh mesh;

    private final Solution horizontalSolution;

    private final ProductionExecutorFactory launcherFactory;

    private final SolutionLogger solutionLogger;

    public VerticalLeafInitializer(Mesh mesh, Solution horizontalSolution, ProductionExecutorFactory launcherFactory, SolutionLogger solutionLogger) {
        this.mesh = mesh;
        this.horizontalSolution = horizontalSolution;
        this.launcherFactory = launcherFactory;
        this.solutionLogger = solutionLogger;
    }

    @Override
    public void initializeLeaves(VerticalIterator verticalIterator) {
        final double[][] rhs = horizontalSolution.getRhs();

        verticalIterator.onKthLeafFromFirst((range) -> launcherFactory
                .launchProduction(new Ay(rhs, new double[]{1, 1. / 2, 1. / 3}, mesh))
                .inVertexRange(range)
                .andWaitTillComplete(), 0);

        verticalIterator.onKthLeafFromFirst((range) -> launcherFactory
                .launchProduction(new Ay(rhs, new double[]{1. / 2, 1. / 3, 1. / 3}, mesh))
                .inVertexRange(range)
                .andWaitTillComplete(), 1);

        verticalIterator.onKthLeafFromFirst((range) -> launcherFactory
                .launchProduction(new Ay(rhs, new double[]{1. / 3, 1. / 3, 1. / 3}, mesh))
                .inVertexRange(range)
                .andWaitTillComplete(), 2);

        verticalIterator.onAllLeavesBetween(
                (range) -> launcherFactory
                        .launchProduction(new Ay(rhs, new double[]{1. / 3, 1. / 3, 1. / 3}, mesh))
                        .inVertexRange(range)
                        .andWaitTillComplete(), 3, 3);

        verticalIterator.onKthLeafFromLast((range) -> launcherFactory
                .launchProduction(new Ay(rhs, new double[]{1. / 3, 1. / 3, 1. / 3}, mesh))
                .inVertexRange(range)
                .andWaitTillComplete(), 2);

        verticalIterator.onKthLeafFromLast((range) -> launcherFactory
                .launchProduction(new Ay(rhs, new double[]{1. / 3, 1. / 3, 1. / 2}, mesh))
                .inVertexRange(range)
                .andWaitTillComplete(), 1);

        verticalIterator.onKthLeafFromLast((range) -> launcherFactory
                .launchProduction(new Ay(rhs, new double[]{1. / 3, 1. / 2, 1}, mesh))
                .inVertexRange(range)
                .andWaitTillComplete(), 0);

        solutionLogger.logMatrixValuesFor(verticalIterator.getCurrentRange(), "Initialize leaves");
    }

}
