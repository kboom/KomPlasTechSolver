package com.agh.iet.komplastech.solver.problem;

import com.agh.iet.komplastech.solver.Solution;

public class ConstantOneProblem extends NonStationaryProblem {

    public ConstantOneProblem(double delta) {
        super(delta);
    }

    @Override
    protected double getInitialValue(double x, double y) {
        return 1;
    }

    @Override
    protected double getValueAtTime(double x, double y, Solution currentSolution, double delta) {
        return 1;
    }

}
