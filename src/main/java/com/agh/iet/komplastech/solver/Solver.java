package com.agh.iet.komplastech.solver;

import com.agh.iet.komplastech.solver.problem.Problem;

public interface Solver {

    Solution solveProblem(Problem problem, RunInformation runInformation);

}
