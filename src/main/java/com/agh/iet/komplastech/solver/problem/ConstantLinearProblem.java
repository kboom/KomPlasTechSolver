package com.agh.iet.komplastech.solver.problem;

import com.agh.iet.komplastech.solver.Solution;

public class ConstantLinearProblem extends NonStationaryProblem {

    public ConstantLinearProblem(double delta) {
        super(delta);
    }

    @Override
    protected double getInitialValue(double x, double y) {
        return x + y;
    }

    @Override
    protected double getValueAtTime(double x, double y, Solution currentSolution, double delta) {
        return x + y;
    }

}
