package com.agh.iet.komplastech.solver.results.visualization;

import com.agh.iet.komplastech.solver.SolutionsInTime;

public class TransientSolutionMapper extends ParameterizedMapper {

    private final SolutionsInTime solutionsInTime;

    public TransientSolutionMapper(SolutionsInTime solutionsInTime) {
        this.solutionsInTime = solutionsInTime;
    }

    @Override
    protected double fAtTimeStep(double x, double y, int timeStep) {
        return solutionsInTime.getSolutionAt(timeStep).getValue(x, y);
    }

    public static TransientSolutionMapper fromSolution(SolutionsInTime solutionsInTime) {
        return new TransientSolutionMapper(solutionsInTime);
    }

}
