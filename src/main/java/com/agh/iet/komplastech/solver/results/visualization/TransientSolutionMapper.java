package com.agh.iet.komplastech.solver.results.visualization;

import com.agh.iet.komplastech.solver.SolutionSeries;

public class TransientSolutionMapper extends ParameterizedMapper {

    private final SolutionSeries solutionSeries;

    public TransientSolutionMapper(SolutionSeries solutionSeries) {
        this.solutionSeries = solutionSeries;
    }

    @Override
    protected double fAtTimeStep(double x, double y, int timeStep) {
        return solutionSeries.getSolutionAt(timeStep).getValue(x, y);
    }

    public static TransientSolutionMapper fromSolution(SolutionSeries solutionSeries) {
        return new TransientSolutionMapper(solutionSeries);
    }

}
