package com.agh.iet.komplastech.solver;

import org.junit.Test;

import static com.agh.iet.komplastech.solver.Point.solutionPoint;
import static com.agh.iet.komplastech.solver.SolutionGrid.solutionGrid;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ProblemSolverTests {

    private ProblemSolver problemSolver = new ProblemSolver();

    @Test
    public void solvesProblem() {
        Solution solution = problemSolver.solveProblem();
        assertThat(solution.getSolutionGrid()).isEqualTo(solutionGrid(
                solutionPoint(0.5d, 0.5d, 0.5d),
                solutionPoint(1.5d, 0.5d, 1.5d),
                solutionPoint(2.5d, 0.5d, 2.5d),
                solutionPoint(3.5d, 0.5d, 3.5d),
                solutionPoint(4.5d, 0.5d, 4.5d),
                solutionPoint(5.5d, 0.5d, 5.5d),
                solutionPoint(6.5d, 0.5d, 6.5d),
                solutionPoint(7.5d, 0.5d, 7.5d),
                solutionPoint(8.5d, 0.5d, 8.5d),
                solutionPoint(9.5d, 0.5d, 9.5d),
                solutionPoint(10.5d, 0.5d, 10.5d),
                solutionPoint(11.5d, 0.5d, 11.5d)
        ));
    }

}
