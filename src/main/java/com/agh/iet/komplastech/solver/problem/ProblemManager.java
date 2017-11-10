package com.agh.iet.komplastech.solver.problem;

import com.agh.iet.komplastech.solver.SolutionFactory;
import com.agh.iet.komplastech.solver.SolutionsInTime;

public interface ProblemManager {

    NonStationaryProblem getProblem();

    SolutionFactory getSolutionFactory();

    void displayResults(SolutionsInTime solutionsInTime);

    void logResults(SolutionsInTime solutionsInTime);

    void setUp();

    void tearDown();

}
