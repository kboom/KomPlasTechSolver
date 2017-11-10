package com.agh.iet.komplastech.solver.problem.flood;

import com.agh.iet.komplastech.solver.Solution;
import com.agh.iet.komplastech.solver.problem.NonStationaryProblem;

public final class FloodingProblem extends NonStationaryProblem {

    private Solution terrain;

    public FloodingProblem(double delta, Solution terrain) {
        super(delta);
        this.terrain = terrain;
    }

    @Override
    protected double getInitialValue(double x, double y) {
        return terrain.getValue(x, y);
    }

    @Override
    protected double getValueAtTime(double x, double y, Solution currentSolution, double delta) {
        double value = currentSolution.getValue(x, y);
        return value + delta * currentSolution.getModifiedValue(x, y);
    }

}
