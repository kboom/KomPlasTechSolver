package com.agh.iet.komplastech.solver.problem;

import com.agh.iet.komplastech.solver.Solution;

import static com.agh.iet.komplastech.solver.factories.HazelcastProblemFactory.CONSTANT_LINEAR;

public class ConstantLinearProblem extends NonStationaryProblem {

    public ConstantLinearProblem(double delta) {
        super(delta);
    }

    @SuppressWarnings("unused")
    public ConstantLinearProblem() {}

    @Override
    protected double getInitialValue(double x, double y) {
        return x + y;
    }

    @Override
    protected double getValueAtTime(double x, double y, Solution currentSolution, double delta) {
        return x + y;
    }

    @Override
    public int getId() {
        return CONSTANT_LINEAR;
    }
}
