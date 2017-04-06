package com.agh.iet.komplastech.solver.initialization;

import com.agh.iet.komplastech.solver.ProductionExecutorFactory;
import com.agh.iet.komplastech.solver.logger.SolutionLogger;
import com.agh.iet.komplastech.solver.productions.initialization.Ay;
import com.agh.iet.komplastech.solver.tracking.VerticalIterator;

public class VerticalLeafInitializer implements LeafInitializer {

    private final ProductionExecutorFactory launcherFactory;

    private final SolutionLogger solutionLogger;

    public VerticalLeafInitializer(ProductionExecutorFactory launcherFactory, SolutionLogger solutionLogger) {
        this.launcherFactory = launcherFactory;
        this.solutionLogger = solutionLogger;
    }

    @Override
    public void initializeLeaves(VerticalIterator verticalIterator) {
        int levelOffset = (int) Math.pow(2, verticalIterator.getCurrentLevel());

        verticalIterator.onKthLeafFromFirst((range) -> launcherFactory
                .launchProduction(new Ay(new double[]{1, 1. / 2, 1. / 3}, levelOffset))
                .inVertexRange(range)
                .andWaitTillComplete(), 0);

        verticalIterator.onKthLeafFromFirst((range) -> launcherFactory
                .launchProduction(new Ay(new double[]{1. / 2, 1. / 3, 1. / 3}, levelOffset))
                .inVertexRange(range)
                .andWaitTillComplete(), 1);

        verticalIterator.onKthLeafFromFirst((range) -> launcherFactory
                .launchProduction(new Ay(new double[]{1. / 3, 1. / 3, 1. / 3}, levelOffset))
                .inVertexRange(range)
                .andWaitTillComplete(), 2);

        verticalIterator.onAllLeavesBetween(
                (range) -> launcherFactory
                        .launchProduction(new Ay(new double[]{1. / 3, 1. / 3, 1. / 3}, levelOffset))
                        .inVertexRange(range)
                        .andWaitTillComplete(), 3, 3);

        verticalIterator.onKthLeafFromLast((range) -> launcherFactory
                .launchProduction(new Ay(new double[]{1. / 3, 1. / 3, 1. / 3}, levelOffset))
                .inVertexRange(range)
                .andWaitTillComplete(), 2);

        verticalIterator.onKthLeafFromLast((range) -> launcherFactory
                .launchProduction(new Ay(new double[]{1. / 3, 1. / 3, 1. / 2}, levelOffset))
                .inVertexRange(range)
                .andWaitTillComplete(), 1);

        verticalIterator.onKthLeafFromLast((range) -> launcherFactory
                .launchProduction(new Ay(new double[]{1. / 3, 1. / 2, 1}, levelOffset))
                .inVertexRange(range)
                .andWaitTillComplete(), 0);

        solutionLogger.logMatrixValuesFor(verticalIterator.getCurrentRange(), "Initialize leaves");

        verticalIterator.moveOneUp();

    }

}
