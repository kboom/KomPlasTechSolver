package com.agh.iet.komplastech.solver;

import com.agh.iet.komplastech.solver.logger.NoopSolutionLogger;
import com.agh.iet.komplastech.solver.logger.SolutionLogger;
import com.agh.iet.komplastech.solver.results.CsvPrinter;
import com.agh.iet.komplastech.solver.storage.InMemoryObjectStore;
import com.agh.iet.komplastech.solver.storage.ObjectStore;
import com.agh.iet.komplastech.solver.support.Mesh;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Paths;

import static com.agh.iet.komplastech.solver.support.Mesh.aMesh;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ProblemSolverTests {

    private static final SolutionLogger DUMMY_SOLUTION_LOGGER = new NoopSolutionLogger();
    private static final TimeLogger DUMMY_TIME_LOGGER = new TimeLogger();

    private CsvPrinter csvPrinter = new CsvPrinter();
    private ObjectStore objectStore = new InMemoryObjectStore();
    private ProductionExecutorFactory productionExecutorFactory =
            new ProductionExecutorFactory(null, null, null, null);

    @Test
    public void solvesSmallerProblem() throws Exception {
        Mesh mesh = aMesh()
                .withElementsX(12)
                .withElementsY(12)
                .withResolutionX(12d)
                .withResolutionY(12d)
                .withOrder(2).build();

        TwoDimensionalProblemSolver problemSolver = createSolver(mesh);

        Solution solution = problemSolver.solveProblem((x, y) -> x + y);
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

        TwoDimensionalProblemSolver problemSolver = createSolver(mesh);

        Solution solution = problemSolver.solveProblem((x, y) -> x + y);
        assertThat(csvPrinter.convertToCsv(solution.getSolutionGrid())).isEqualTo(readTestFile("big_problem_results.csv"));
    }

    private TwoDimensionalProblemSolver createSolver(Mesh mesh) {
        return new TwoDimensionalProblemSolver(
                null,
                productionExecutorFactory,
                null,
                mesh,
                null,
                DUMMY_SOLUTION_LOGGER,
                null,
                null,
                DUMMY_TIME_LOGGER
        );
    }

    private String readTestFile(String path) throws Exception {
        return new String(Files.readAllBytes(Paths.get(ClassLoader.getSystemResource(path).toURI())));
    }


}
