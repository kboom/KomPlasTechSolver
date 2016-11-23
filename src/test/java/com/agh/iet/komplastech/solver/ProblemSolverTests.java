package com.agh.iet.komplastech.solver;

import com.agh.iet.komplastech.solver.execution.ProductionExecutorFactory;
import com.agh.iet.komplastech.solver.results.CsvPrinter;
import com.agh.iet.komplastech.solver.support.Mesh;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Paths;

import static com.agh.iet.komplastech.solver.support.Mesh.aMesh;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ProblemSolverTests {

    private CsvPrinter csvPrinter = new CsvPrinter();
    private ProductionExecutorFactory productionExecutorFactory = new ProductionExecutorFactory();

    @Test
    public void solvesSmallerProblem() throws Exception {
        Mesh mesh = aMesh()
                .withElementsX(12)
                .withElementsY(12)
                .withResolutionX(12d)
                .withResolutionY(12d)
                .withOrder(2).build();

        TwoDimensionalProblemSolver problemSolver = new TwoDimensionalProblemSolver(productionExecutorFactory, mesh);

        Solution solution = problemSolver.solveProblem(timeLogger);
        assertThat(csvPrinter.convertToCsv(solution.getSolutionGrid())).isEqualTo(readTestFile("small_problem_results.csv"));
    }

    @Test
    public void solvesBiggerProblem() throws Exception {
        Mesh mesh = aMesh()
                .withElementsX(24)
                .withElementsY(24)
                .withResolutionX(24d)
                .withResolutionY(24d)
                .withOrder(2).build();

        TwoDimensionalProblemSolver problemSolver = new TwoDimensionalProblemSolver(productionExecutorFactory, mesh);

        Solution solution = problemSolver.solveProblem(timeLogger);
        assertThat(csvPrinter.convertToCsv(solution.getSolutionGrid())).isEqualTo(readTestFile("big_problem_results.csv"));
    }

    private String readTestFile(String path) throws Exception {
        return new String(Files.readAllBytes(Paths.get(ClassLoader.getSystemResource(path).toURI())));
    }


}
