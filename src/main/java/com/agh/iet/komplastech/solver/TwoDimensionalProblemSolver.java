package com.agh.iet.komplastech.solver;

import com.agh.iet.komplastech.solver.execution.ProductionExecutorFactory;
import com.agh.iet.komplastech.solver.initialization.HorizontalLeafInitializer;
import com.agh.iet.komplastech.solver.initialization.LeafInitializer;
import com.agh.iet.komplastech.solver.initialization.VerticalLeafInitializer;
import com.agh.iet.komplastech.solver.logger.SolutionLogger;
import com.agh.iet.komplastech.solver.problem.Problem;
import com.agh.iet.komplastech.solver.productions.HorizontalProductionFactory;
import com.agh.iet.komplastech.solver.productions.ProductionFactory;
import com.agh.iet.komplastech.solver.productions.VerticalProductionFactory;
import com.agh.iet.komplastech.solver.support.Mesh;

class TwoDimensionalProblemSolver implements Solver {

    private final Mesh mesh;

    private final ProductionExecutorFactory launcherFactory;

    private final SolutionLogger solutionLogger;

    private final TimeLogger timeLogger;

    TwoDimensionalProblemSolver(ProductionExecutorFactory launcherFactory,
                                Mesh meshData,
                                SolutionLogger solutionLogger,
                                TimeLogger timeLogger) {
        this.launcherFactory = launcherFactory;
        this.mesh = meshData;
        this.timeLogger = timeLogger;
        this.solutionLogger = solutionLogger;
    }

    @Override
    public Solution solveProblem(Problem rhs, RunInformation runInformation) {
        final ProductionFactory horizontalProductionFactory = new HorizontalProductionFactory(mesh);
        final LeafInitializer horizontalLeafInitializer = new HorizontalLeafInitializer(mesh, rhs);

        DirectionSolver horizontalProblemSolver = new DirectionSolver(
                horizontalProductionFactory,
                launcherFactory,
                horizontalLeafInitializer,
                mesh,
                solutionLogger,
                timeLogger
        );


        final Solution horizontalSolution = horizontalProblemSolver.solveProblem(rhs, runInformation);

        ProductionFactory verticalProductionFactory = new VerticalProductionFactory(mesh);
        LeafInitializer verticalLeafInitializer = new VerticalLeafInitializer(mesh, horizontalSolution);
        DirectionSolver verticalProblemSolver = new DirectionSolver(
                verticalProductionFactory,
                launcherFactory,
                verticalLeafInitializer,
                mesh,
                solutionLogger,
                timeLogger
        );

        return verticalProblemSolver.solveProblem(rhs, runInformation);
    }

}