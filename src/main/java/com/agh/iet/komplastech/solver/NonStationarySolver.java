package com.agh.iet.komplastech.solver;

import com.agh.iet.komplastech.solver.problem.NonStationaryProblem;
import com.agh.iet.komplastech.solver.support.Mesh;

import static com.agh.iet.komplastech.solver.SolutionsInTime.solutionsInTime;
import static java.lang.String.format;

class NonStationarySolver {

    private final int timeStepCount;

    private final double delta;

    private final Solver solver;

    private final Mesh mesh;

    private final boolean printSolutionHashes;

    NonStationarySolver(int timeStepCount,
                        double delta,
                        Solver solver,
                        Mesh mesh, boolean printSolutionHashes) {
        this.timeStepCount = timeStepCount;
        this.delta = delta;
        this.solver = solver;
        this.mesh = mesh;
        this.printSolutionHashes = printSolutionHashes;
    }

    SolutionsInTime solveInTime(NonStationaryProblem nonStationaryProblem) {
        SolutionsInTime.SolutionsInTimeBuilder solutionsInTimeBuilder = solutionsInTime()
                .withMesh(mesh)
                .withDelta(delta);
        for (int i = 0; i < timeStepCount; i++) {
            Solution solution = solver.solveProblem(nonStationaryProblem);
            if(printSolutionHashes) {
                System.out.println(String.format("Solution %d - approx. checksum - %d", i, solution.getChecksum()));
            }
            solutionsInTimeBuilder.addSolution(solution); // TODO do not store for now, fix it
            nonStationaryProblem.nextStep(solution);
        }
        return solutionsInTimeBuilder.build();
    }

}
