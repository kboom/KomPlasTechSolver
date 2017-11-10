package com.agh.iet.komplastech.solver;

import com.agh.iet.komplastech.solver.execution.ProductionExecutorFactory;
import com.agh.iet.komplastech.solver.logger.ConsoleSolutionLogger;
import com.agh.iet.komplastech.solver.logger.NoopSolutionLogger;
import com.agh.iet.komplastech.solver.problem.ProblemFactory;
import com.agh.iet.komplastech.solver.problem.heat.HeatFactory;
import com.agh.iet.komplastech.solver.results.CsvPrinter;
import com.agh.iet.komplastech.solver.results.visualization.TimeLapseViewer;
import com.agh.iet.komplastech.solver.support.Mesh;
import com.beust.jcommander.Parameter;

import static com.agh.iet.komplastech.solver.support.Mesh.aMesh;

class SolverLauncher {

    @Parameter(names={"--log", "-l"})
    private boolean isLogging = false;

    @Parameter(names={"--plot", "-p"})
    private boolean isPlotting = false;

    @Parameter(names={"--problem-size", "-s"})
    private int problemSize = 12;

    @Parameter(names={"--max-threads", "-t"})
    private int maxThreads = 12;

    @Parameter(names={"--delta", "-d"})
    private double delta = 0.001;

    @Parameter(names={"--steps", "-o"})
    private int steps = 10;

    void launch() {
        ProductionExecutorFactory productionExecutorFactory = new ProductionExecutorFactory();
        TimeLogger timeLogger = new TimeLogger();

        productionExecutorFactory.setAvailableThreads(maxThreads);

        Mesh mesh = aMesh()
                .withElementsX(problemSize)
                .withElementsY(problemSize)
                .withResolutionX(problemSize)
                .withResolutionY(problemSize)
                .withOrder(2).build();


        final ProblemFactory problemFactory = HeatFactory.builder()
                .delta(delta)
                .mesh(mesh)
                .problemSize(problemSize)
                .build();


        TwoDimensionalProblemSolver problemSolver = new TwoDimensionalProblemSolver(
                productionExecutorFactory,
                mesh,
                problemFactory.getSolutionFactory(),
                isLogging ? new ConsoleSolutionLogger(mesh) : new NoopSolutionLogger(),
                timeLogger
        );

        try {
            NonStationarySolver nonStationarySolver =
                    new NonStationarySolver(steps, delta, problemSolver, mesh);


            SolutionsInTime solutionsInTime = nonStationarySolver.solveInTime(problemFactory.getProblem());

            productionExecutorFactory.joinAll();

            System.out.print(String.format("%d,%d,%d,%d",
                    timeLogger.getTotalCreationMs(),
                    timeLogger.getTotalInitializationMs(),
                    timeLogger.getTotalFactorizationMs(),
                    timeLogger.getTotalSolutionMs()
            ));


            Solution solution = solutionsInTime.getFinalSolution();

            if (isLogging) {
                CsvPrinter csvPrinter = new CsvPrinter();
                System.out.println(csvPrinter.convertToCsv(solution.getSolutionGrid()));
            }

            if (isPlotting) {
                TimeLapseViewer timeLapseViewer = new TimeLapseViewer(solutionsInTime);
                timeLapseViewer.setVisible(true);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
