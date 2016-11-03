package com.agh.iet.komplastech.solver;

import com.agh.iet.komplastech.solver.results.CsvPrinter;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Paths;

import static com.agh.iet.komplastech.solver.Mesh.aMesh;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ProblemSolverTests {

    private CsvPrinter csvPrinter = new CsvPrinter();

    @Test
    public void solvesProblem() throws Exception {
        Mesh mesh = aMesh()
                .withSizeX(12)
                .withSizeY(12)
                .withResolutionX(12d)
                .withResolutionY(12d)
                .withOrder(2).build();

        ProblemSolver problemSolver = new ProblemSolver(mesh);

        Solution solution = problemSolver.solveProblem();
        assertThat(csvPrinter.convertToCsv(solution.getSolutionGrid())).isEqualTo(readTestFile("test-results1.csv"));
    }

    private String readTestFile(String path) throws Exception {
        return new String(Files.readAllBytes(Paths.get(ClassLoader.getSystemResource(path).toURI())));
    }


}
