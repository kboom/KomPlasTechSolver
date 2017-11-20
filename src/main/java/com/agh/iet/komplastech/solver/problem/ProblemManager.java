package com.agh.iet.komplastech.solver.problem;

import com.agh.iet.komplastech.solver.SolutionFactory;
import com.agh.iet.komplastech.solver.SolutionSeries;

public interface ProblemManager {

    IterativeProblem getProblem();

    SolutionFactory getSolutionFactory();

    void displayResults(SolutionSeries solutionSeries);

    void logResults(SolutionSeries solutionSeries);

    void setUp();

    void tearDown();

}
