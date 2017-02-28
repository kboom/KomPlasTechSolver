package com.agh.iet.komplastech.solver.initialization;

import com.agh.iet.komplastech.solver.ProductionExecutorFactory;
import com.agh.iet.komplastech.solver.Solution;
import com.agh.iet.komplastech.solver.productions.initialization.Ay;
import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.tracking.VerticalIterator;

public class VerticalLeafInitializer implements LeafInitializer {

    private final Mesh mesh;

    private final Solution horizontalSolution;

    private final ProductionExecutorFactory launcherFactory;

    public VerticalLeafInitializer(Mesh mesh, Solution horizontalSolution, ProductionExecutorFactory launcherFactory) {
        this.mesh = mesh;
        this.horizontalSolution = horizontalSolution;
        this.launcherFactory = launcherFactory;
    }

    @Override
    public void initializeLeaves(VerticalIterator verticalIterator) {
        final double[][] rhs = horizontalSolution.getRhs();

        verticalIterator.onKthFromFirst((vertices) -> new Ay(rhs, new double[]{1, 1. / 2, 1. / 3}, mesh), 0);
        verticalIterator.onKthFromFirst(new Ay(rhs, new double[]{1. / 2, 1. / 3, 1. / 3}, mesh), 1);
        verticalIterator.onKthFromFirst(new Ay(rhs, new double[]{1. / 3, 1. / 3, 1. / 3}, mesh), 2);

        verticalIterator.onAllBetween(new Ay(rhs, new double[]{1. / 3, 1. / 3, 1. / 3}, mesh), 3, 3);

        verticalIterator.onKthFromLast(new Ay(rhs, new double[]{1, 1. / 2, 1. / 3}, mesh), 2);
        verticalIterator.onKthFromLast(new Ay(rhs, new double[]{1. / 2, 1. / 3, 1. / 3}, mesh), 1);
        verticalIterator.onKthFromLast(new Ay(rhs, new double[]{1. / 3, 1. / 3, 1. / 3}, mesh), 0);
    }

}
