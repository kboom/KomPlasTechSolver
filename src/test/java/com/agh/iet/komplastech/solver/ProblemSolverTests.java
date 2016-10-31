package com.agh.iet.komplastech.solver;

import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ProblemSolverTests {

    private ProblemSolver problemSolver = new ProblemSolver();

    private CsvPrinter csvPrinter = new CsvPrinter();

    @Test
    public void solvesProblem() throws Exception {
        Solution solution = problemSolver.solveProblem();
        assertThat(csvPrinter.convertToCsv(solution.getSolutionGrid())).isEqualTo(readTestFile("test-results1.csv"));
    }

    private String readTestFile(String path) throws Exception {
        return new String(Files.readAllBytes(Paths.get(ClassLoader.getSystemResource(path).toURI())));
    }


}
