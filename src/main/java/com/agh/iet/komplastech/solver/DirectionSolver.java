package com.agh.iet.komplastech.solver;

import com.agh.iet.komplastech.solver.initialization.LeafInitializer;
import com.agh.iet.komplastech.solver.logger.SolutionLogger;
import com.agh.iet.komplastech.solver.problem.Problem;
import com.agh.iet.komplastech.solver.productions.Production;
import com.agh.iet.komplastech.solver.productions.ProductionFactory;
import com.agh.iet.komplastech.solver.storage.ObjectStore;
import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.support.Vertex;
import com.agh.iet.komplastech.solver.support.VertexMap;
import com.agh.iet.komplastech.solver.support.VertexRange;
import com.agh.iet.komplastech.solver.tracking.TreeIteratorFactory;
import com.agh.iet.komplastech.solver.tracking.VerticalIterator;

import java.util.List;

import static com.agh.iet.komplastech.solver.VertexId.vertexId;
import static com.agh.iet.komplastech.solver.productions.CompositeProduction.compositeProductionOf;
import static com.agh.iet.komplastech.solver.support.Vertex.aVertex;

public class DirectionSolver implements Solver {

    private static final int ROOT_LEVEL_HEIGHT = 1;
    private static final int LEAF_LEVEL_HEIGHT = 1;

    private final Mesh mesh;

    private final ProductionExecutorFactory launcherFactory;

    private final ProductionFactory productionFactory;

    private final ObjectStore objectStore;

    private final VertexMap vertexMap;

    private final LeafInitializer leafInitializer;

    private final TreeIteratorFactory treeIteratorFactory;

    private final SolutionLogger solutionLogger;


    private VerticalIterator treeIterator;


    DirectionSolver(ObjectStore objectStore,
                    ProductionFactory productionFactory,
                    ProductionExecutorFactory launcherFactory,
                    TreeIteratorFactory treeIteratorFactory,
                    LeafInitializer leafInitializer,
                    Mesh meshData,
                    SolutionLogger solutionLogger) {
        this.objectStore = objectStore;
        this.vertexMap = objectStore.getVertexMap();
        this.productionFactory = productionFactory;
        this.launcherFactory = launcherFactory;
        this.treeIteratorFactory = treeIteratorFactory;
        this.leafInitializer = leafInitializer;
        this.mesh = meshData;
        this.solutionLogger = solutionLogger;
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

        Vertex rootVertex = aVertex(vertexId(1))
                .inMesh(mesh)
                .withBeggining(0)
                .withEnding(mesh.getResolutionX()).build();

        objectStore.storeVertex(rootVertex);

        treeIterator = treeIteratorFactory.createFor(rootVertex.getId(), getIntermediateLevelsCount() + 2);

        treeIterator.executeOnRootGoingDown(
                (range) -> launcherFactory
                        .launchProduction(production)
                        .inVertexRange(range)
                        .andWaitTillComplete()
        );
    }

    private void buildIntermediateLevels() {
        final Production production = productionFactory.createBranchIntermediateProduction();
        treeIterator.forEachGoingDown(
                getIntermediateLevelsCount(),
                (range) -> launcherFactory
                        .launchProduction(production)
                        .inVertexRange(range)
                        .andWaitTillComplete()
        );
    }

    private void buildLeaves() {
        final Production production = productionFactory.createLeafProduction();
        treeIterator.forEachGoingDownOnce(
                (range) -> launcherFactory
                        .launchProduction(production)
                        .inVertexRange(range)
                        .andWaitTillComplete()
        );
    }

    private void initializeLeaves() {
        leafInitializer.initializeLeaves(treeIterator);
    }

    private void mergeLeaves() {
        final Production production = productionFactory.createLeafMergingProduction();
        treeIterator.forEachStayingAt(
                (range) -> {
                    launcherFactory
                            .launchProduction(production)
                            .inVertexRange(range)
                            .andWaitTillComplete();
                    solutionLogger.logMatrixValuesFor(range, "Merge leaves");
                }
        );
    }

    private void eliminateLeaves() {
        final Production production = productionFactory.createLeafEliminatingProduction();
        treeIterator.forEachStayingAt(
                (range) -> {
                    launcherFactory
                            .launchProduction(production)
                            .inVertexRange(range)
                            .andWaitTillComplete();
                    solutionLogger.logMatrixValuesFor(range, "Eliminate leaves");
                }
        );
    }

    private void mergeAndEliminateFirstIntermediate() {
        final Production mergingProduction = productionFactory.createFirstIntermediateMergingProduction();
        final Production eliminatingProduction = productionFactory.createFirstIntermediateEliminatingProduction();

        treeIterator.forEachStayingAt(
                (range) -> {
                    launcherFactory
                            .launchProduction(compositeProductionOf(mergingProduction, eliminatingProduction))
                            .inVertexRange(range)
                            .andWaitTillComplete();

                    solutionLogger.logMatrixValuesFor(range, "Merge and eliminate one up the leaves");
                }
        );
    }

    private void mergeAndEliminateIntermediate() {
        final Production mergingProduction = productionFactory.mergeUpProduction();
        final Production eliminatingProduction = productionFactory.eliminateUpProduction();

        treeIterator.forEachGoingUp(
                getIntermediateLevelsCount() - 1,
                (range) -> {
                    launcherFactory
                            .launchProduction(compositeProductionOf(mergingProduction, eliminatingProduction))
                            .inVertexRange(range)
                            .andWaitTillComplete();

                    solutionLogger.logMatrixValuesFor(range, "Merge and eliminate one up the leaves");
                }
        );
    }

    private void solveRoot() {
        final Production mergingProduction = productionFactory.createRootSolvingProduction();
        final Production backwardSubstituteProduction = productionFactory.createRootBackwardsSubstitutingProduction();

        treeIterator.executeOnRootGoingDown(
                (range) -> {
                    launcherFactory
                            .launchProduction(compositeProductionOf(mergingProduction, backwardSubstituteProduction))
                            .inVertexRange(range)
                            .andWaitTillComplete();
                    solutionLogger.logMatrixValuesFor(range, "Solve root");
                }
        );
    }

    private void backwardSubstituteIntermediate() {
        final Production production = productionFactory.backwardSubstituteUpProduction();

        treeIterator.forEachGoingDown(
                getIntermediateLevelsCount() - 1,
                (range) -> {
                    launcherFactory
                            .launchProduction(production)
                            .inVertexRange(range)
                            .andWaitTillComplete();
                    solutionLogger.logMatrixValuesFor(range, "Backward substitute intermediate");
                }
        );
    }

    private void backwardSubstituteOneUpLeaves() {
        final Production production = productionFactory.backwardSubstituteIntermediateProduction();

        treeIterator.forEachGoingDownOnce(
                (range) -> {
                    launcherFactory
                            .launchProduction(production)
                            .inVertexRange(range)
                            .andWaitTillComplete();
                    solutionLogger.logMatrixValuesFor(range, "Backward substitute oneo up the leaves");
                }
        );
    }

    private void backwardSubstituteLeaves() {
        final Production production = productionFactory.backwardSubstituteLeavesProduction();
        treeIterator.forEachStayingAt(
                (range) -> {
                    launcherFactory
                            .launchProduction(production)
                            .inVertexRange(range)
                            .andWaitTillComplete();
                    solutionLogger.logMatrixValuesFor(range, "Backward substitute leaves");
                }
        );
    }

    private double[][] getRhs() {
        final double[][] rhs = new double[mesh.getElementsX() + mesh.getSplineOrder() + 1][];

        // for now just take all of them and compute here, later do this on worker nodes
        VertexRange vertexRange = treeIterator.getCurrentRange();
        final List<Vertex> sortedLeaves = objectStore.getVertexMap().getAllInRange(vertexRange);

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
