package com.agh.iet.komplastech.solver;

import com.agh.iet.komplastech.solver.execution.ProductionExecutorFactory;
import com.agh.iet.komplastech.solver.results.CsvPrinter;
import com.agh.iet.komplastech.solver.results.visualization.PlotIn3D;

import static com.agh.iet.komplastech.solver.support.Mesh.aMesh;

public class Main {

    private static final int PROBLEM_SIZE_INDEX = 0;
    private static final int AVAILABLE_THREADS_INDEX = 1;
    private static final int LOG_RESULTS = 2;

    public static void main(String[] args) {
        ProductionExecutorFactory productionExecutorFactory = new ProductionExecutorFactory();
        TimeLogger timeLogger = new TimeLogger();

        int problemSize = 12;
        if(args.length > 0) {
            problemSize = Integer.parseInt(args[PROBLEM_SIZE_INDEX]);
        }
        if(args.length > 1) {
            productionExecutorFactory.setAvailableThreads(Integer.parseInt(args[AVAILABLE_THREADS_INDEX]));
        }


        TwoDimensionalProblemSolver s = new TwoDimensionalProblemSolver(
                productionExecutorFactory,
                aMesh()
                .withElementsX(problemSize)
                .withElementsY(problemSize)
                .withResolutionX(problemSize)
                .withResolutionY(problemSize)
                .withOrder(2).build());
        try {
            Solution solution = s.solveProblem(timeLogger);
            System.out.print(String.format("%d,%d,%d,%d",
                    timeLogger.getTotalCreationMs(),
                    timeLogger.getTotalInitializationMs(),
                    timeLogger.getTotalFactorizationMs(),
                    timeLogger.getTotalSolutionMs()
            ));

            if(args.length > 2 && Boolean.parseBoolean(args[LOG_RESULTS])) {
                CsvPrinter csvPrinter = new CsvPrinter();
                System.out.println(csvPrinter.convertToCsv(solution.getSolutionGrid()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        productionExecutorFactory.joinAll();

        PlotIn3D plot = new PlotIn3D();
        plot.open();

    }

}