package com.agh.iet.komplastech.solver.problem.flood;

import com.agh.iet.komplastech.solver.Solution;
import com.agh.iet.komplastech.solver.problem.NonStationaryProblem;
import com.agh.iet.komplastech.solver.problem.TimeModifier;

public final class FloodingProblem extends NonStationaryProblem {

    private Solution terrain;
    private TimeModifier rainF;

    FloodingProblem(double delta, Solution terrain, TimeModifier rainF) {
        super(delta);
        this.terrain = terrain;
        this.rainF = rainF;
    }

    @Override
    protected double getInitialValue(double x, double y) {
        return terrain.getValue(x, y);
    }

    @Override
    protected double getValueAtTime(double x, double y, Solution currentSolution, double delta) {
        return currentSolution.getValue(x, y) + delta * currentSolution.getModifiedValue(x, y) + rainF.getAddedValue(x, y, getCurrentTime());
    }

}
