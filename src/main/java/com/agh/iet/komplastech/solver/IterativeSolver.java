package com.agh.iet.komplastech.solver;

import com.agh.iet.komplastech.solver.problem.IterativeProblem;
import com.agh.iet.komplastech.solver.problem.Problem;
import com.agh.iet.komplastech.solver.support.Mesh;
import lombok.AllArgsConstructor;

import java.util.Optional;

import static com.agh.iet.komplastech.solver.SolutionSeries.solutionsInTime;

@AllArgsConstructor
class IterativeSolver {

    private final Solver solver;

    private final Mesh mesh;

    SolutionSeries solveIteratively(IterativeProblem iterativeProblem) {
        SolutionSeries.SolutionsInTimeBuilder solutionsInTimeBuilder = solutionsInTime().withMesh(mesh);
        Optional<Problem> currentProblem = Optional.of(iterativeProblem.getInitialProblem());
        while (currentProblem.isPresent()) {
            Solution solution = solver.solveProblem(currentProblem.get());
            solutionsInTimeBuilder.addSolution(solution);
            currentProblem = iterativeProblem.getNextProblem(solution);
        }
        return solutionsInTimeBuilder.build();
    }

}
