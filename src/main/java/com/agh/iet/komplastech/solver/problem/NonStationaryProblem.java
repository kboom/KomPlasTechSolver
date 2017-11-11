package com.agh.iet.komplastech.solver.problem;

import com.agh.iet.komplastech.solver.Solution;

public abstract class NonStationaryProblem implements Problem {

    private double currentTime = 0;
    private double delta;
    private Solution currentSolution;

    public NonStationaryProblem(double delta) {
        this.delta = delta;
    }

    public void nextStep(Solution solution) {
        currentSolution = solution;
        currentTime += delta;
    }

    public double getCurrentTime() {
        return currentTime;
    }

    @Override
    public double getValue(double x, double y) {
        return currentSolution == null ?
                getInitialValue(x, y) :
                getValueAtTime(x, y, currentSolution, delta);
    }

    protected abstract double getInitialValue(double x, double y);
    protected abstract double getValueAtTime(double x, double y, Solution currentSolution, double delta);

}
