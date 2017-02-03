package com.agh.iet.komplastech.solver;

import static com.agh.iet.komplastech.solver.SolutionsInTime.solutionsInTime;

class NonStationarySolver {

    private final int timeStepCount;

    private final double delta;

    private final Solver solver;

    NonStationarySolver(int timeStepCount,
                               double delta,
                               Solver solver) {
        this.timeStepCount = timeStepCount;
        this.delta = delta;
        this.solver = solver;
    }

    SolutionsInTime solveInTime(NonStationaryProblem nonStationaryProblem) {
        SolutionsInTime.SolutionsInTimeBuilder solutionsInTimeBuilder = solutionsInTime()
                .withDelta(delta);
        for(int i = 0; i < timeStepCount; i++) {
            Solution solution = solver.solveProblem(nonStationaryProblem);
            solutionsInTimeBuilder.addSolution(solution);
            nonStationaryProblem.nextStep(solution);
        }
        return solutionsInTimeBuilder.build();
    }

}
