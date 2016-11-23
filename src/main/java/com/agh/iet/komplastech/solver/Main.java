package com.agh.iet.komplastech.solver;

import com.agh.iet.komplastech.solver.results.CsvPrinter;

import static com.agh.iet.komplastech.solver.support.Mesh.aMesh;

public class Main {

    public static void main(String[] args) {
        TwoDimensionalProblemSolver s = new TwoDimensionalProblemSolver(aMesh()
                .withElementsX(24)
                .withElementsY(24)
                .withResolutionX(24d)
                .withResolutionY(24d)
                .withOrder(2).build());
        try {
            Solution solution = s.solveProblem();
            CsvPrinter csvPrinter = new CsvPrinter();
            System.out.println(csvPrinter.convertToCsv(solution.getSolutionGrid()));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}