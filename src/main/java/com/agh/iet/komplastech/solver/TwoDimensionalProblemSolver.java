package com.agh.iet.komplastech.solver;

import com.agh.iet.komplastech.solver.execution.ProductionExecutorFactory;
import com.agh.iet.komplastech.solver.initialization.HorizontalLeafInitializer;
import com.agh.iet.komplastech.solver.initialization.LeafInitializer;
import com.agh.iet.komplastech.solver.initialization.VerticalLeafInitializer;
import com.agh.iet.komplastech.solver.productions.HorizontalProductionFactory;
import com.agh.iet.komplastech.solver.productions.ProductionFactory;
import com.agh.iet.komplastech.solver.productions.VerticalProductionFactory;
import com.agh.iet.komplastech.solver.support.Mesh;

class TwoDimensionalProblemSolver {

    private final Mesh mesh;

    private final ProductionExecutorFactory launcherFactory;

    private final SolutionLogger solutionLogger;

    TwoDimensionalProblemSolver(ProductionExecutorFactory launcherFactory,
                                Mesh meshData,
                                SolutionLogger solutionLogger) {
        this.launcherFactory = launcherFactory;
        this.mesh = meshData;
        this.solutionLogger = solutionLogger;
    }

    Solution solveProblem(TimeLogger timeLogger) throws Exception {
        ProductionFactory horizontalProductionFactory = new HorizontalProductionFactory(mesh);
        LeafInitializer horizontalLeafInitializer = new HorizontalLeafInitializer(mesh);

        DirectionSolver horizontalProblemSolver = new DirectionSolver(
                horizontalProductionFactory,
                launcherFactory,
                horizontalLeafInitializer,
                mesh,
                solutionLogger
        );
        Solution horizontalSolution = horizontalProblemSolver.solve(timeLogger);

        ProductionFactory verticalProductionFactory = new VerticalProductionFactory(mesh);
        LeafInitializer verticalLeafInitializer = new VerticalLeafInitializer(mesh, horizontalSolution);
        DirectionSolver verticalProblemSolver = new DirectionSolver(
                verticalProductionFactory,
                launcherFactory,
                verticalLeafInitializer,
                mesh,
                solutionLogger
        );

        return verticalProblemSolver.solve(timeLogger);
    }

}