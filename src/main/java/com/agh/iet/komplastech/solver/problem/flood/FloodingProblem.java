package com.agh.iet.komplastech.solver.problem.flood;

import com.agh.iet.komplastech.solver.Solution;
import com.agh.iet.komplastech.solver.problem.IterativeProblem;
import com.agh.iet.komplastech.solver.problem.Problem;
import com.agh.iet.komplastech.solver.problem.TimeModifier;
import lombok.AllArgsConstructor;

import java.util.Optional;

public final class FloodingProblem implements IterativeProblem {

    private final Solution terrain;
    private final double delta;
    private final TimeModifier rainF;
    private final int steps;

    private int currentStep = 0;

    FloodingProblem(double delta, Solution terrain, TimeModifier rainF, int steps) {
        this.terrain = terrain;
        this.delta = delta;
        this.rainF = rainF;
        this.steps = steps;
    }

    private int currentTime = 0;

    @Override
    public Problem getInitialProblem() {
        return terrain::getValue;
    }

    @Override
    public Optional<Problem> getNextProblem(Solution solution) {
        currentTime += delta;
        currentStep++;
        return currentStep < steps ? Optional.of(getProblem(solution)) : Optional.empty();
    }

//    private Problem getProblem(Solution solution) {
//        return (x, y) -> solution.getValue(x, y) + delta * solution.getModifiedValue(x, y) + rainF.getAddedValue(x, y, currentTime);
//    }

    private Problem getProblem(Solution solution) {
        return (x, y) -> solution.getValue(x, y) + delta * solution.getModifiedValue(x, y) + rainF.getAddedValue(x, y, currentTime);
    }

}
