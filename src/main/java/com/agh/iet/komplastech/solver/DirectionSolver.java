package com.agh.iet.komplastech.solver;

import com.agh.iet.komplastech.solver.initialization.LeafInitializer;
import com.agh.iet.komplastech.solver.problem.Problem;
import com.agh.iet.komplastech.solver.productions.Production;
import com.agh.iet.komplastech.solver.productions.ProductionFactory;
import com.agh.iet.komplastech.solver.storage.ObjectStore;
import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.support.Vertex;
import com.agh.iet.komplastech.solver.tracking.TreeIteratorFactory;
import com.agh.iet.komplastech.solver.tracking.VerticalIterator;

import java.util.List;

import static com.agh.iet.komplastech.solver.productions.CompositeProduction.compositeProductionOf;
import static com.agh.iet.komplastech.solver.support.Vertex.aVertex;

public class DirectionSolver implements Solver {

    private static final int ROOT_LEVEL_HEIGHT = 1;
    private static final int LEAF_LEVEL_HEIGHT = 1;

    private final Mesh mesh;

    private final ProductionExecutorFactory launcherFactory;

    private final ProductionFactory productionFactory;

    private final ObjectStore objectStore;

    private final LeafInitializer leafInitializer;

    private final TreeIteratorFactory treeIteratorFactory;


    private VerticalIterator treeIterator;


    DirectionSolver(ObjectStore objectStore,
                    ProductionFactory productionFactory,
                    ProductionExecutorFactory launcherFactory,
                    TreeIteratorFactory treeIteratorFactory,
                    LeafInitializer leafInitializer,
                    Mesh meshData) {
        this.objectStore = objectStore;
        this.productionFactory = productionFactory;
        this.launcherFactory = launcherFactory;
        this.treeIteratorFactory = treeIteratorFactory;
        this.leafInitializer = leafInitializer;
        this.mesh = meshData;
    }

    @Override
    public Solution solveProblem(Problem problem) {
        createRoot();
        buildIntermediateLevels();
        buildLeaves();
        initializeLeaves();
        mergeLeaves();
        eliminateLeaves();
        mergeAndEliminateFirstIntermediate();
        mergeAndEliminateIntermediate();
        solveRoot();
        backwardSubstituteIntermediate();
        backwardSubstituteOneUpLeaves();
        backwardSubstituteLeaves();
        return new Solution(problem, mesh, getRhs());
    }

    private void createRoot() {
        final Production production = productionFactory.createBranchRootProduction();

        Vertex rootVertex = objectStore.createNewVertex(aVertex()
                .inMesh(mesh)
                .withBeggining(0)
                .withEnding(mesh.getResolutionX()));

        treeIterator = treeIteratorFactory.createFor(rootVertex.getId());

        treeIterator.executeOnRootGoingDown(
                (vertices) -> launcherFactory
                        .launchProduction(production)
                        .onVertices(vertices)
                        .andWaitTillComplete()
        );
    }

    private void buildIntermediateLevels() {
        final Production production = productionFactory.createBranchIntermediateProduction();
        treeIterator.forEachGoingDown(
                getIntermediateLevelsCount(),
                (vertices) -> launcherFactory
                        .launchProduction(production)
                        .onVertices(vertices)
                        .andWaitTillComplete()
        );
    }

    private void buildLeaves() {
        final Production production = productionFactory.createLeafProduction();
        treeIterator.forEachStayingAt(
                (vertices) -> launcherFactory
                        .launchProduction(production)
                        .onVertices(vertices)
                        .andWaitTillComplete()
        );
    }

    private void initializeLeaves() {
        leafInitializer.initializeLeaves(treeIterator);
    }

    private void mergeLeaves() {
        final Production production = productionFactory.createLeafMergingProduction();
        treeIterator.forEachStayingAt(
                (vertices) -> launcherFactory
                        .launchProduction(production)
                        .onVertices(vertices)
                        .andWaitTillComplete()
        );
    }

    private void eliminateLeaves() {
        final Production production = productionFactory.createLeafEliminatingProduction();
        treeIterator.forEachStayingAt(
                (vertices) -> launcherFactory
                        .launchProduction(production)
                        .onVertices(vertices)
                        .andWaitTillComplete()
        );
    }

    private void mergeAndEliminateFirstIntermediate() {
        final Production mergingProduction = productionFactory.createFirstIntermediateMergingProduction();
        final Production eliminatingProduction = productionFactory.createFirstIntermediateEliminatingProduction();

        treeIterator.forEachStayingAt(
                (vertices) -> launcherFactory
                        .launchProduction(compositeProductionOf(mergingProduction, eliminatingProduction))
                        .onVertices(vertices)
                        .andWaitTillComplete()
        );
    }

    private void mergeAndEliminateIntermediate() {
        final Production mergingProduction = productionFactory.mergeUpProduction();
        final Production eliminatingProduction = productionFactory.eliminateUpProduction();

        treeIterator.forEachGoingUp(
                getIntermediateLevelsCount() - 1,
                (vertices) -> launcherFactory
                        .launchProduction(compositeProductionOf(mergingProduction, eliminatingProduction))
                        .onVertices(vertices)
                        .andWaitTillComplete()
        );
    }

    private void solveRoot() {
        final Production mergingProduction = productionFactory.createRootSolvingProduction();
        final Production backwardSubstituteProduction = productionFactory.createRootBackwardsSubstitutingProduction();

        treeIterator.executeOnRootGoingDown(
                (vertices) -> launcherFactory
                        .launchProduction(compositeProductionOf(mergingProduction, backwardSubstituteProduction))
                        .onVertices(vertices)
                        .andWaitTillComplete()
        );
    }

    private void backwardSubstituteIntermediate() {
        final Production production = productionFactory.backwardSubstituteUpProduction();

        treeIterator.forEachGoingDownTheRoot(
                getIntermediateLevelsCount() - 1,
                (vertices) -> launcherFactory
                        .launchProduction(production)
                        .onVertices(vertices)
                        .andWaitTillComplete()
        );
    }

    private void backwardSubstituteOneUpLeaves() {
        final Production production = productionFactory.backwardSubstituteIntermediateProduction();

        treeIterator.forEachGoingDownOnce(
                (vertices) -> launcherFactory
                        .launchProduction(production)
                        .onVertices(vertices)
                        .andWaitTillComplete()
        );
    }

    private void backwardSubstituteLeaves() {
        final Production production = productionFactory.backwardSubstituteLeavesProduction();
        treeIterator.forEachStayingAt(
                (vertices) -> launcherFactory
                        .launchProduction(production)
                        .onVertices(vertices)
                        .andWaitTillComplete()
        );
    }

    private double[][] getRhs() {
        final double[][] rhs = new double[mesh.getElementsX() + mesh.getSplineOrder() + 1][];
        final List<Vertex> sortedLeaves = treeIterator.getSortedLeaves();

        int i = 0;
        for (Vertex vertex : sortedLeaves) {
            if (i == 0) {
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

    private int log2(double value) {
        return (int) Math.floor(Math.log(value) / Math.log(2));
    }

    private int getIntermediateLevelsCount() {
        return log2(2 * mesh.getElementsX() / 3) - ROOT_LEVEL_HEIGHT - LEAF_LEVEL_HEIGHT;
    }

}
