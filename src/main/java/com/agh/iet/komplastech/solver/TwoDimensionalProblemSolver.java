package com.agh.iet.komplastech.solver;

import com.agh.iet.komplastech.solver.initialization.HorizontalLeafInitializer;
import com.agh.iet.komplastech.solver.initialization.LeafInitializer;
import com.agh.iet.komplastech.solver.initialization.VerticalLeafInitializer;
import com.agh.iet.komplastech.solver.logger.SolutionLogger;
import com.agh.iet.komplastech.solver.logger.process.ProcessLogger;
import com.agh.iet.komplastech.solver.problem.Problem;
import com.agh.iet.komplastech.solver.productions.HorizontalProductionFactory;
import com.agh.iet.komplastech.solver.productions.ProductionFactory;
import com.agh.iet.komplastech.solver.productions.VerticalProductionFactory;
import com.agh.iet.komplastech.solver.storage.ObjectStore;
import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.support.VertexRegionMapper;
import com.agh.iet.komplastech.solver.tracking.TreeIteratorFactory;

class TwoDimensionalProblemSolver implements Solver {

    private final Mesh mesh;

    private final HazelcastFacade hazelcastFacade;

    private final ProductionExecutorFactory launcherFactory;

    private final ObjectStore objectStore;

    private final SolutionLogger solutionLogger;

    private final ProcessLogger processLogger;

    private final TimeLogger timeLogger;

    private final VertexRegionMapper vertexRegionMapper;

    TwoDimensionalProblemSolver(HazelcastFacade hazelcastFacade,
                                ProductionExecutorFactory launcherFactory,
                                Mesh meshData,
                                VertexRegionMapper vertexRegionMapper,
                                SolutionLogger solutionLogger,
                                ProcessLogger processLogger,
                                ObjectStore objectStore,
                                TimeLogger timeLogger) {
        this.hazelcastFacade = hazelcastFacade;
        this.launcherFactory = launcherFactory;
        this.mesh = meshData;
        this.vertexRegionMapper = vertexRegionMapper;
        this.solutionLogger = solutionLogger;
        this.processLogger = processLogger;
        this.objectStore = objectStore;
        this.timeLogger = timeLogger;
    }

    @Override
    public Solution solveProblem(Problem rhs) {
        hazelcastFacade.forceGC();
        prepareObjectStore(rhs);
        timeLogger.logStart();
        timeLogger.logFirstStage();
        Solution horizontalSolution = solveProblemHorizontally(rhs);
        objectStore.clearVertices();
        propagateSolution(horizontalSolution);
        timeLogger.nextStage();
        timeLogger.logSecondStage();
        Solution solution = solveProblemVertically(horizontalSolution);
        timeLogger.logStop();
        hazelcastFacade.forceGC();
        return solution;
    }

    private void propagateSolution(Solution horizontalSolution) {
        objectStore.setSolution(horizontalSolution);
        hazelcastFacade.forceLoadCommons();
    }

    private void prepareObjectStore(Problem rhs) {
        objectStore.clearAll();
        objectStore.setProblem(rhs);
        objectStore.setMesh(mesh);
        hazelcastFacade.forceGC();
        hazelcastFacade.forceLoadCommons();
    }

    private Solution solveProblemHorizontally(Problem rhs) {
        HorizontalProductionFactory productionFactory = new HorizontalProductionFactory(vertexRegionMapper);
        HorizontalLeafInitializer horizontalLeafInitializer = new HorizontalLeafInitializer(launcherFactory, solutionLogger);
        TreeIteratorFactory treeIteratorFactory = new TreeIteratorFactory();
        DirectionSolver horizontalProblemSolver = new DirectionSolver(
                objectStore,
                productionFactory,
                launcherFactory,
                treeIteratorFactory,
                horizontalLeafInitializer,
                mesh,
                solutionLogger,
                processLogger,
                timeLogger
        );

        return horizontalProblemSolver.solveProblem(rhs);
    }

    private Solution solveProblemVertically(Solution horizontalSolution) {
        LeafInitializer verticalLeafInitializer = new VerticalLeafInitializer(launcherFactory, solutionLogger);
        ProductionFactory verticalProductionFactory = new VerticalProductionFactory(vertexRegionMapper);
        TreeIteratorFactory treeIteratorFactory = new TreeIteratorFactory();

        DirectionSolver verticalProblemSolver = new DirectionSolver(
                objectStore,
                verticalProductionFactory,
                launcherFactory,
                treeIteratorFactory,
                verticalLeafInitializer,
                mesh,
                solutionLogger,
                processLogger,
                timeLogger
        );



        return verticalProblemSolver.solveProblem(horizontalSolution.getProblem());
    }

}