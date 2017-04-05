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

    private final ProductionExecutorFactory launcherFactory;

    private final ObjectStore objectStore;

    private final SolutionLogger solutionLogger;

    private final ProcessLogger processLogger;

    private final TimeLogger timeLogger;

    private final VertexRegionMapper vertexRegionMapper;

    TwoDimensionalProblemSolver(ProductionExecutorFactory launcherFactory,
                                Mesh meshData,
                                VertexRegionMapper vertexRegionMapper,
                                SolutionLogger solutionLogger,
                                ProcessLogger processLogger,
                                ObjectStore objectStore,
                                TimeLogger timeLogger) {
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
        Solution horizontalSolution = solveProblemHorizontally(rhs);
        objectStore.clearAll();
        return solveProblemVertically(horizontalSolution, rhs);
    }

    private Solution solveProblemHorizontally(Problem rhs) {
        HorizontalProductionFactory productionFactory = new HorizontalProductionFactory(mesh, rhs, vertexRegionMapper);
        HorizontalLeafInitializer horizontalLeafInitializer = new HorizontalLeafInitializer(mesh, rhs, launcherFactory, solutionLogger);
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

    private Solution solveProblemVertically(Solution horizontalSolution, Problem rhs) {
        LeafInitializer verticalLeafInitializer = new VerticalLeafInitializer(mesh, horizontalSolution, launcherFactory, solutionLogger);
        ProductionFactory verticalProductionFactory = new VerticalProductionFactory(mesh, horizontalSolution, vertexRegionMapper);
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

        return verticalProblemSolver.solveProblem(rhs);
    }

}