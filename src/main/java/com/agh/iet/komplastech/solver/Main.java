package com.agh.iet.komplastech.solver;

import static com.agh.iet.komplastech.solver.support.Mesh.aMesh;

public class Main {

    public static void main(String[] args) {
        ProblemSolver s = new ProblemSolver(aMesh()
                .withSizeX(12)
                .withSizeY(12)
                .withResolutionX(12d)
                .withResolutionY(12d)
                .withOrder(2).build());
        try {
            s.solveProblem();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}