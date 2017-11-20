package com.agh.iet.komplastech.solver.problem;

import com.agh.iet.komplastech.solver.Solution;

import java.util.Optional;

public class NoopIterativeProblem implements IterativeProblem {

    @Override
    public Problem getInitialProblem() {
        return (x, y) -> 1;
    }

    @Override
    public Optional<Problem> getNextProblem(Solution solution) {
        return Optional.empty();
    }

}
