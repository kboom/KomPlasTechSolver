package com.agh.iet.komplastech.solver.problem;

import com.agh.iet.komplastech.solver.Solution;

import static com.agh.iet.komplastech.solver.factories.HazelcastProblemFactory.CONSTANT_ONE;

public class ConstantOneProblem extends NonStationaryProblem {

    public ConstantOneProblem(double delta) {
        super(delta);
    }

    @SuppressWarnings("unused")
    public ConstantOneProblem() {}

    @Override
    protected double getInitialValue(double x, double y) {
        return 1;
    }

    @Override
    protected double getValueAtTime(double x, double y, Solution currentSolution, double delta) {
        return 1;
    }

    @Override
    public int getId() {
        return CONSTANT_ONE;
    }

}
