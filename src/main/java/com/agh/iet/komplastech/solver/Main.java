package com.agh.iet.komplastech.solver;

public class Main {

    public static void main(String[] args) {

        // BUILDING ELEMENT PARTITION TREE AND RUNNING THE SOLVER
        ProblemSolver s = new ProblemSolver();
        try {
            s.solveProblem();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}