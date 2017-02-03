package com.agh.iet.komplastech.solver;

abstract class NonStationaryProblem implements RightHandSide {

    private double delta;
    private Solution currentSolution;

    NonStationaryProblem(double delta) {
        this.delta = delta;
    }

    public void nextStep(Solution solution) {
        currentSolution = solution;
    }

    @Override
    public double getValue(double x, double y) {
        return currentSolution == null ?
                getInitialValue(x, y) :
                getValueAtTime(x, y, currentSolution, delta);
    }

    abstract double getInitialValue(double x, double y);
    abstract double getValueAtTime(double x, double y, Solution currentSolution, double delta);

}
