package com.agh.iet.komplastech.solver;

import com.agh.iet.komplastech.solver.execution.ProductionExecutorFactory;
import com.agh.iet.komplastech.solver.logger.SolutionLogger;
import com.agh.iet.komplastech.solver.problem.Problem;
import com.agh.iet.komplastech.solver.support.Mesh;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SolverFactory {

    private final Mesh mesh;
    private final SolutionLogger solutionLogger;
    private final TimeLogger timeLogger;

    public Solver createSolver(SolutionFactory solutionFactory) {
        ProductionExecutorFactory productionExecutorFactory = new ProductionExecutorFactory();

        TwoDimensionalProblemSolver problemSolver = new TwoDimensionalProblemSolver(
                productionExecutorFactory,
                mesh,
                solutionFactory,
                solutionLogger,
                timeLogger
        );

        return new SolverFacade(problemSolver) {
            /*
             * Place any facade-like methods in here
             */
        };
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public abstract class SolverFacade implements Solver {

        private final Solver solver;

        @Override
        public Solution solveProblem(Problem problem) {
            return solver.solveProblem(problem);
        }

    }

}
