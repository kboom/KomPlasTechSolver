package com.agh.iet.komplastech.solver.problem;

import com.agh.iet.komplastech.solver.Solution;

import java.util.Optional;

public interface IterativeProblem {

    Problem getInitialProblem();

    Optional<Problem> getNextProblem(Solution solution);

}
