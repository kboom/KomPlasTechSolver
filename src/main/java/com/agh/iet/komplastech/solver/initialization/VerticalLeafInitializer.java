package com.agh.iet.komplastech.solver.initialization;

import com.agh.iet.komplastech.solver.ProductionExecutorFactory;
import com.agh.iet.komplastech.solver.Solution;
import com.agh.iet.komplastech.solver.productions.Production;
import com.agh.iet.komplastech.solver.productions.initialization.Ay;
import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.support.Vertex;
import com.agh.iet.komplastech.solver.tracking.VerticalIterator;

import java.util.ArrayList;
import java.util.List;

public class VerticalLeafInitializer implements LeafInitializer {

    private final Mesh mesh;

    private final Solution horizontalSolution;

    private final ProductionExecutorFactory launcherFactory;

    public VerticalLeafInitializer(Mesh mesh, Solution horizontalSolution, ProductionExecutorFactory launcherFactory) {
        this.mesh = mesh;
        this.horizontalSolution = horizontalSolution;
        this.launcherFactory = launcherFactory;
    }

    // cannot be done this way as we need indices... every production is different... it should be somehow dependent on the vertex itself
    @Override
    public void initializeLeaves(VerticalIterator verticalIterator) {
        final double[][] rhs = horizontalSolution.getRhs();

        verticalIterator.forAllAtLevelStartingFrom(new Ay(rhs, new double[]{1, 1. / 2, 1. / 3}, 0, mesh), 0);
        verticalIterator.forAllAtLevelStartingFrom(new Ay(rhs, new double[]{1. / 2, 1. / 3, 1. / 3}, 1, mesh), 1);
        verticalIterator.forAllAtLevelStartingFrom(new Ay(rhs, new double[]{1. / 3, 1. / 3, 1. / 3}, 2, mesh), 2);

        verticalIterator.forAllBetween(new Ay(rhs, new double[]{1. / 3, 1. / 3, 1. / 3}, 2, mesh), 3, 3);

        verticalIterator.forAllAtLevelStartingFrom(new Ay(rhs, new double[]{1, 1. / 2, 1. / 3}, 0, mesh), 0);
        verticalIterator.forAllAtLevelStartingFrom(new Ay(rhs, new double[]{1. / 2, 1. / 3, 1. / 3}, 1, mesh), 1);
        verticalIterator.forAllAtLevelStartingFrom(new Ay(rhs, new double[]{1. / 3, 1. / 3, 1. / 3}, 2, mesh), 2);



        final int leafCount = verticalIterator.size();

        List<Production> initializationProductions = new ArrayList<>(verticalIterator.size());

        Vertex firstVertex = verticalIterator.get(0);
        initializationProductions.add(new Ay(firstVertex.leftChild, rhs, new double[]{1, 1. / 2, 1. / 3}, 0, mesh));
        initializationProductions.add(new Ay(firstVertex.middleChild, rhs, new double[]{1. / 2, 1. / 3, 1. / 3}, 1, mesh));
        initializationProductions.add(new Ay(firstVertex.rightChild, rhs, new double[]{1. / 3, 1. / 3, 1. / 3}, 2, mesh));

        for (int i = 1; i < leafCount - 1; i++) {
            Vertex vertex = verticalIterator.get(i);
            initializationProductions.add(new Ay(vertex.leftChild, rhs, new double[]{1. / 3, 1. / 3, 1. / 3}, 3 * i, mesh));
            initializationProductions.add(new Ay(vertex.middleChild, rhs, new double[]{1. / 3, 1. / 3, 1. / 3}, 3 * i + 1, mesh));
            initializationProductions.add(new Ay(vertex.rightChild, rhs, new double[]{1. / 3, 1. / 3, 1. / 3}, 3 * i + 2, mesh));
        }


        Vertex lastVertex = verticalIterator.get(leafCount - 1);
        initializationProductions.add(new Ay(lastVertex.leftChild, rhs, new double[]{1. / 3, 1. / 3, 1. / 3}, leafCount * 3 - 3, mesh));
        initializationProductions.add(new Ay(lastVertex.middleChild, rhs, new double[]{1. / 3, 1. / 3, 1. / 2}, leafCount * 3 - 2, mesh));
        initializationProductions.add(new Ay(lastVertex.rightChild, rhs, new double[]{1. / 3, 1. / 2, 1}, leafCount * 3 - 1, mesh));

        return initializationProductions;
    }

}
