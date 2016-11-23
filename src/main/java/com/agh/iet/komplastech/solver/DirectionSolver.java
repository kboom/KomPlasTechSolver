package com.agh.iet.komplastech.solver;

import com.agh.iet.komplastech.solver.execution.ProductionExecutorFactory;
import com.agh.iet.komplastech.solver.initialization.LeafInitializer;
import com.agh.iet.komplastech.solver.productions.Production;
import com.agh.iet.komplastech.solver.productions.ProductionFactory;
import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.support.Vertex;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.agh.iet.komplastech.solver.support.Vertex.aVertex;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

class DirectionSolver {

    private static final int ROOT_LEVEL_HEIGHT = 1;
    private static final int LEAF_LEVEL_HEIGHT = 1;

    private List<Vertex> lastLevelVertices;
    private List<Vertex> leafLevelVertices;

    private final Mesh mesh;

    private ProductionExecutorFactory launcherFactory;

    private ProductionFactory productionFactory;

    private LeafInitializer leafInitializer;

    DirectionSolver(ProductionFactory productionFactory,
                    ProductionExecutorFactory launcherFactory,
                    LeafInitializer leafInitializer,
                    Mesh meshData) {
        this.productionFactory = productionFactory;
        this.launcherFactory = launcherFactory;
        this.leafInitializer = leafInitializer;
        this.mesh = meshData;
    }

    Solution solve(TimeLogger timeLogger) {
        timeLogger.logCreation();
        Vertex root = createRoot();
        lastLevelVertices = buildIntermediateLevels(root);
        leafLevelVertices = buildLeaves();
        timeLogger.logInitialization();
        initializeLeaves();
        timeLogger.logFactorization();
        mergeLeaves();
        eliminateLeaves();
        factorizeTree();
        solveRoot(root);
        timeLogger.logBackwardSubstitution();
        backwardSubstituteIntermediate(root);
        backwardSubstituteLeaves();
        timeLogger.logSolution();
        return new Solution(mesh, getRhs());
    }

    private void initializeLeaves() {
        launcherFactory
                .createLauncherFor(leafInitializer.initializeLeaves(leafLevelVertices))
                .launchProductions();
    }

    private int log2(double value) {
        return (int) Math.floor(Math.log(value) / Math.log(2));
    }

    private double[][] getRhs() {
        // fixed for now
        double[][] rhs = new double[mesh.getElementsX() + mesh.getSplineOrder() + 1][];

        int i = 0;
        for(Vertex vertex : leafLevelVertices) {
            if(i == 0) {
                rhs[1] = vertex.m_x[1];
                rhs[2] = vertex.m_x[2];
                rhs[3] = vertex.m_x[3];
                rhs[4] = vertex.m_x[4];
                rhs[5] = vertex.m_x[5];
            } else {
                int offset = 6 + (i - 1) * 3;
                rhs[offset] = vertex.m_x[3];
                rhs[offset + 1] = vertex.m_x[4];
                rhs[offset + 2] = vertex.m_x[5];
            }
            i++;
        }
        return rhs;
    }

    private void backwardSubstituteLeaves() {
        launcherFactory
                .createLauncherFor(
                        leafLevelVertices.stream().map(vertex
                                -> productionFactory.backwardSubstituteLeavesProduction(vertex))
                                .collect(toList())
                )
                .launchProductions();
    }

    private void backwardSubstituteIntermediate(Vertex root) {
        List<Vertex> verticesAtLevel = root.getChildren();
        for (int level = 0; level < getIntermediateLevelsCount(); level++) {
            launcherFactory
                    .createLauncherFor(
                            verticesAtLevel.stream().map(vertex
                                    -> productionFactory.backwardSubstituteIntermediateProduction(vertex))
                                    .collect(toList())
                    )
                    .launchProductions();

            verticesAtLevel = collectChildren(verticesAtLevel);
        }
    }

    private List<Vertex> collectChildren(List<Vertex> parents) {
        List<Vertex> childVertices = new ArrayList<>();
        for (Vertex vertex : parents) {
            childVertices.addAll(vertex.getChildren());
        }
        return childVertices;
    }

    private Vertex createRoot() {
        Vertex rootVertex = aVertex()
                .withMesh(mesh)
                .withBeggining(0)
                .withEnding(mesh.getResolutionX())
                .build();


        Production production = productionFactory.createRootProduction(rootVertex);

        launcherFactory
                .createLauncherFor(production)
                .launchProductions();
        return rootVertex;
    }

    private void factorizeTree() {
        List<Vertex> verticesAtLevel = lastLevelVertices;

        while (verticesAtLevel.size() > 1) {
            launcherFactory
                    .createLauncherFor(
                            verticesAtLevel.stream().map(vertex
                                    -> productionFactory.mergeIntermediateProduction(vertex))
                                    .collect(toList())
                    )
                    .launchProductions();

            launcherFactory
                    .createLauncherFor(
                            verticesAtLevel.stream().map(vertex
                                    -> productionFactory.eliminateIntermediateProduction(vertex))
                                    .collect(toList())
                    )
                    .launchProductions();

            verticesAtLevel = collectParents(verticesAtLevel);
        }
    }

    private void solveRoot(Vertex root) {
        Production aroot = productionFactory.rootSolverProduction(root);

        launcherFactory
                .createLauncherFor(aroot)
                .launchProductions();

        Production eroot = productionFactory.backwardSubstituteProduction(root);
        launcherFactory
                .createLauncherFor(eroot)
                .launchProductions();
    }

    private List<Vertex> collectParents(List<Vertex> verticesAtLevel) {
        List<Vertex> parentVertices = new ArrayList<>();
        for (Vertex vertex : verticesAtLevel) {
            Vertex parentVertex = vertex.getParent();
            if (!parentVertices.contains(parentVertex)) {
                parentVertices.add(parentVertex);
            }
        }
        return parentVertices;
    }

    private void mergeLeaves() {
        launcherFactory
                .createLauncherFor(leafLevelVertices.stream().map(vertex
                        -> productionFactory.mergeLeavesProduction(vertex))
                        .collect(toList()))
                .launchProductions();
    }

    private void eliminateLeaves() {
        launcherFactory
                .createLauncherFor(leafLevelVertices.stream().map(vertex
                        -> productionFactory.eliminateLeavesProduction(vertex))
                        .collect(toList()))
                .launchProductions();
    }

    private List<Vertex> buildIntermediateLevels(Vertex root) {
        List<Vertex> previousLevelVertices = singletonList(root);
        int intermediateTreeLevelCount = getIntermediateLevelsCount();
        for (int i = 0; i < intermediateTreeLevelCount; i++) {
            int elementsAtPrevious = (int) Math.pow(2, i);
            List<Production> newLevelProductions = new ArrayList<>(2 * elementsAtPrevious);
            List<Vertex> newLevelVertices = new ArrayList<>(2 * elementsAtPrevious);
            for (int j = 0; j < elementsAtPrevious; j++) {
                Vertex previousVertex = previousLevelVertices.get(j);
                Production leftChildProduction = productionFactory.createIntermediateProduction(previousVertex.leftChild);
                newLevelVertices.add(previousVertex.leftChild);
                newLevelProductions.add(leftChildProduction);
                Production rightChildProduction = productionFactory.createIntermediateProduction(previousVertex.rightChild);
                newLevelVertices.add(previousVertex.rightChild);
                newLevelProductions.add(rightChildProduction);
            }
            previousLevelVertices = newLevelVertices;

            launcherFactory
                    .createLauncherFor(newLevelProductions)
                    .launchProductions();
        }

        return previousLevelVertices;
    }

    private int getIntermediateLevelsCount() {
        return log2(2 * mesh.getElementsX() / 3) - ROOT_LEVEL_HEIGHT - LEAF_LEVEL_HEIGHT;
    }

    private List<Vertex> buildLeaves() {
        int leafLevelCount = lastLevelVertices.size();
        List<Production> leafInitializationProductions = new ArrayList<>(leafLevelCount);
        for (Vertex vertex : lastLevelVertices) {
            Production leftChildProduction = productionFactory.createLeafProductions(vertex.leftChild);
            leafInitializationProductions.add(leftChildProduction);
            Production rightChildProduction = productionFactory.createLeafProductions(vertex.rightChild);
            leafInitializationProductions.add(rightChildProduction);
        }

        launcherFactory
                .createLauncherFor(leafInitializationProductions)
                .launchProductions();

        return leafInitializationProductions.stream().map(production -> production.m_vertex).collect(Collectors.toList());
    }

}
