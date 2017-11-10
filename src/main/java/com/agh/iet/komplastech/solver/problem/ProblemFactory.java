package com.agh.iet.komplastech.solver.problem;

import com.agh.iet.komplastech.solver.SolutionFactory;

public interface ProblemFactory {

    NonStationaryProblem getProblem();

    SolutionFactory getSolutionFactory();

}
