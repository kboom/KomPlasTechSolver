package com.agh.iet.komplastech.solver;

import com.agh.iet.komplastech.solver.problem.NonStationaryProblem;
import com.agh.iet.komplastech.solver.support.Mesh;

import static com.agh.iet.komplastech.solver.SolutionsInTime.solutionsInTime;

class NonStationarySolver {

    private final int timeStepCount;

    private final double delta;

    private final Solver solver;

    private final Mesh mesh;

    NonStationarySolver(int timeStepCount,
                        double delta,
                        Solver solver,
                        Mesh mesh) {
        this.timeStepCount = timeStepCount;
        this.delta = delta;
        this.solver = solver;
        this.mesh = mesh;
    }

    SolutionsInTime solveInTime(NonStationaryProblem nonStationaryProblem) {
        SolutionsInTime.SolutionsInTimeBuilder solutionsInTimeBuilder = solutionsInTime()
                .withMesh(mesh)
                .withDelta(delta);
        for (int i = 0; i < timeStepCount; i++) {
            Solution solution = solver.solveProblem(nonStationaryProblem);
            solutionsInTimeBuilder.addSolution(solution);
            nonStationaryProblem.nextStep(solution);
        }
        return solutionsInTimeBuilder.build();
    }

}
