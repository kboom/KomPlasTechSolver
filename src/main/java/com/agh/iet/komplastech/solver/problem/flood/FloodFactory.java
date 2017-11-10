package com.agh.iet.komplastech.solver.problem.flood;

import com.agh.iet.komplastech.solver.SolutionFactory;
import com.agh.iet.komplastech.solver.problem.NonStationaryProblem;
import com.agh.iet.komplastech.solver.problem.ProblemFactory;

public class FloodFactory implements ProblemFactory {

    private SolutionFactory solutionFactory;
    private NonStationaryProblem problem;

    public FloodFactory(int problemSize) {

    }

    @Override
    public SolutionFactory getSolutionFactory() {
        return solutionFactory;
    }

    @Override
    public NonStationaryProblem getProblem() {
        return problem;
    }

}
