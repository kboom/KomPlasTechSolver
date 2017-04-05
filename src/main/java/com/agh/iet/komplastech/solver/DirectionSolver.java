package com.agh.iet.komplastech.solver;

import com.agh.iet.komplastech.solver.initialization.LeafInitializer;
import com.agh.iet.komplastech.solver.logger.SolutionLogger;
import com.agh.iet.komplastech.solver.logger.process.ProcessLogger;
import com.agh.iet.komplastech.solver.problem.Problem;
import com.agh.iet.komplastech.solver.productions.Production;
import com.agh.iet.komplastech.solver.productions.ProductionFactory;
import com.agh.iet.komplastech.solver.storage.ObjectStore;
import com.agh.iet.komplastech.solver.support.*;
import com.agh.iet.komplastech.solver.tracking.TreeIteratorFactory;
import com.agh.iet.komplastech.solver.tracking.VerticalIterator;

import java.util.List;

import static com.agh.iet.komplastech.solver.VertexId.vertexId;
import static com.agh.iet.komplastech.solver.productions.CompositeProduction.compositeProductionOf;
import static com.agh.iet.komplastech.solver.support.Matrix.from2DArray;
import static com.agh.iet.komplastech.solver.support.RegionId.regionId;
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

    private final SolutionLogger solutionLogger;

    private final ProcessLogger processLogger;

    private final TimeLogger timeLogger;


    private VerticalIterator treeIterator;


    DirectionSolver(ObjectStore objectStore,
                    ProductionFactory productionFactory,
                    ProductionExecutorFactory launcherFactory,
                    TreeIteratorFactory treeIteratorFactory,
                    LeafInitializer leafInitializer,
                    Mesh meshData,
                    SolutionLogger solutionLogger,
                    ProcessLogger processLogger,
                    TimeLogger timeLogger) {
        this.objectStore = objectStore;
        this.productionFactory = productionFactory;
        this.launcherFactory = launcherFactory;
        this.treeIteratorFactory = treeIteratorFactory;
        this.leafInitializer = leafInitializer;
        this.mesh = meshData;
        this.solutionLogger = solutionLogger;
        this.processLogger = processLogger;
        this.timeLogger = timeLogger;
    }

    @Override
    public Solution solveProblem(Problem problem) {
        timeLogger.logCreation();
        createRoot();
        buildIntermediateLevels();
        buildLeaves();
        timeLogger.logInitialization();
        initializeLeaves();
        timeLogger.logFactorization();
        mergeAndEliminateLeaves();
        mergeAndEliminateFirstIntermediate();
        mergeAndEliminateIntermediate();
        solveRoot();
        timeLogger.logBackwardSubstitution();
        backwardSubstituteIntermediate();
        backwardSubstituteOneUpLeaves();
        backwardSubstituteLeaves();
        timeLogger.logSolution();
        return new Solution(problem, mesh, getRhs());
    }

    private void createRoot() {
        final Production production = productionFactory.createBranchRootProduction();

        Vertex rootVertex = aVertex(vertexId(1), regionId(1))
                .inMesh(mesh)
                .withBeggining(0)
                .withEnding(mesh.getResolutionX()).build();

        objectStore.storeVertex(rootVertex);

        treeIterator = treeIteratorFactory.createFor(rootVertex.getVertexId(), getIntermediateLevelsCount() + 2);

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
        treeIterator.forEachGoingDownOnce(
                (range) -> {
                    Production production = productionFactory.createLeafProduction(range);
                    launcherFactory
                            .launchProduction(production)
                            .inVertexRange(range)
                            .andWaitTillComplete();
                }
        );
    }

    private void initializeLeaves() {
        leafInitializer.initializeLeaves(treeIterator);
    }

    private void mergeAndEliminateLeaves() {
        final Production mergingProduction = productionFactory.createLeafMergingProduction();
        final Production eliminatingProduction = productionFactory.createLeafEliminatingProduction();
        treeIterator.forEachGoingUpOnce(
                (range) -> {
                    launcherFactory
                            .launchProduction(compositeProductionOf(mergingProduction, eliminatingProduction))
                            .inVertexRange(range)
                            .andWaitTillComplete();
                    processLogger.logStageReached("Merge & elimination of leaves for range " + range);
                    solutionLogger.logMatrixValuesFor(range, "Merge & Eliminate leaves");
                }
        );
    }

    private void mergeAndEliminateFirstIntermediate() {
        final Production mergingProduction = productionFactory.createFirstIntermediateMergingProduction();
        final Production eliminatingProduction = productionFactory.createFirstIntermediateEliminatingProduction();

        treeIterator.forEachGoingUpOnce(
                (range) -> {
                    launcherFactory
                            .launchProduction(compositeProductionOf(mergingProduction, eliminatingProduction))
                            .inVertexRange(range)
                            .andWaitTillComplete();
                    processLogger.logStageReached("Merging & elimination one up leaves for range " + range);
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

                    processLogger.logStageReached("Merging & elimination intermediate for range " + range);
                    solutionLogger.logMatrixValuesFor(range, "Merge and eliminate intermediate");
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
                    processLogger.logStageReached("Solving root");
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
                    processLogger.logStageReached("Intermediate backward substitution for range " + range);
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
                    processLogger.logStageReached("One up leaves backward substitution for range " + range);
                    solutionLogger.logMatrixValuesFor(range, "Backward substitute one up the leaves");
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
                    processLogger.logStageReached("Backward substituting leaves for range " + range);
                    solutionLogger.logMatrixValuesFor(range, "Backward substitute leaves");
                }
        );
    }

    private Matrix getRhs() {
        int size = mesh.getElementsX() + mesh.getSplineOrder() + 1;
        final double[][] rhs = new double[size][size];

        // for now just take all of them and compute here, later do this on worker nodes
        VertexRange vertexRange = treeIterator.getCurrentRange();
        final List<Vertex> sortedLeaves = objectStore.getVertexMap().getAllInRange(vertexRange);

        int i = 0;
        for (Vertex vertex : sortedLeaves) {
            if (i == 0) {
                rhs[1] = vertex.m_x.getRow(1);
                rhs[2] = vertex.m_x.getRow(2);
                rhs[3] = vertex.m_x.getRow(3);
                rhs[4] = vertex.m_x.getRow(4);
                rhs[5] = vertex.m_x.getRow(5);
            } else {
                int offset = 6 + (i - 1) * 3;
                rhs[offset] = vertex.m_x.getRow(3);
                rhs[offset + 1] = vertex.m_x.getRow(4);
                rhs[offset + 2] = vertex.m_x.getRow(5);
            }
            i++;
        }

        return from2DArray(rhs);
    }

    private int log2(double value) {
        return (int) Math.floor(Math.log(value) / Math.log(2));
    }

    private int getIntermediateLevelsCount() {
        return log2(2 * mesh.getElementsX() / 3) - ROOT_LEVEL_HEIGHT - LEAF_LEVEL_HEIGHT;
    }

}
