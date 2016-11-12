package com.agh.iet.komplastech.solver;

import com.agh.iet.komplastech.solver.results.CsvPrinter;

import static com.agh.iet.komplastech.solver.support.Mesh.aMesh;

public class Main {

    public static void main(String[] args) {
        TwoDimensionalProblemSolver s = new TwoDimensionalProblemSolver(aMesh()
                .withElementsX(12)
                .withElementsY(12)
                .withResolutionX(12d)
                .withResolutionY(12d)
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