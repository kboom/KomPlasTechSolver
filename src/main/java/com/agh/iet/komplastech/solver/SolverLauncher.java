package com.agh.iet.komplastech.solver;

import com.agh.iet.komplastech.solver.execution.ProductionExecutorFactory;
import com.agh.iet.komplastech.solver.logger.ConsoleSolutionLogger;
import com.agh.iet.komplastech.solver.logger.NoopSolutionLogger;
import com.agh.iet.komplastech.solver.problem.NonStationaryProblem;
import com.agh.iet.komplastech.solver.results.CsvPrinter;
import com.agh.iet.komplastech.solver.results.visualization.TimeLapseViewer;
import com.agh.iet.komplastech.solver.support.Mesh;
import com.beust.jcommander.Parameter;

import java.util.HashMap;

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
    private int steps = 100;

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

        TwoDimensionalProblemSolver problemSolver = new TwoDimensionalProblemSolver(
                productionExecutorFactory,
                mesh,
                isLogging ? new ConsoleSolutionLogger(mesh) : new NoopSolutionLogger(),
                timeLogger
        );

        try {
            NonStationarySolver nonStationarySolver =
                    new NonStationarySolver(steps, delta, problemSolver, mesh);


            int finalProblemSize = problemSize;

            SolutionsInTime solutionsInTime = nonStationarySolver.solveInTime(new NonStationaryProblem(delta) {

                @Override
                protected double getInitialValue(double x, double y) {
                    double dist = (x - mesh.getCenterX()) * (x - mesh.getCenterX())
                            + (y - mesh.getCenterY()) * (y - mesh.getCenterY());

                    return dist < finalProblemSize ? finalProblemSize - dist : 0;
                }

                @Override
                protected double getValueAtTime(double x, double y, Solution currentSolution, double delta) {
                    double value = currentSolution.getValue(x, y);
                    return value + delta * currentSolution.getLaplacian(x, y);
                }

            });

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
