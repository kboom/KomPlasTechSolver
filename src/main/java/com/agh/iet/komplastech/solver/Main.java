package com.agh.iet.komplastech.solver;

import com.agh.iet.komplastech.solver.execution.ProductionExecutorFactory;
import com.agh.iet.komplastech.solver.results.CsvPrinter;
import com.agh.iet.komplastech.solver.results.visualization.ChartFrame;
import com.agh.iet.komplastech.solver.support.Mesh;
import org.jzy3d.maths.Range;

import static com.agh.iet.komplastech.solver.results.visualization.ChartBuilder.aChart;
import static com.agh.iet.komplastech.solver.results.visualization.SolutionMapper.fromSolution;
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


        Mesh mesh = aMesh()
                .withElementsX(problemSize)
                .withElementsY(problemSize)
                .withResolutionX(problemSize)
                .withResolutionY(problemSize)
                .withOrder(2).build();

        TwoDimensionalProblemSolver s = new TwoDimensionalProblemSolver(
                productionExecutorFactory,
                mesh,
                new ConsoleSolutionLogger(mesh)
        );

        try {
            Solution solution = s.solveProblem(timeLogger);
            System.out.print(String.format("%d,%d,%d,%d",
                    timeLogger.getTotalCreationMs(),
                    timeLogger.getTotalInitializationMs(),
                    timeLogger.getTotalFactorizationMs(),
                    timeLogger.getTotalSolutionMs()
            ));


            productionExecutorFactory.joinAll();

            if(args.length > 2 && Boolean.parseBoolean(args[LOG_RESULTS])) {
                CsvPrinter csvPrinter = new CsvPrinter();
                System.out.println(csvPrinter.convertToCsv(solution.getSolutionGrid()));


                ChartFrame plot = new ChartFrame(aChart()
                        .withMapper(fromSolution(solution))
                        .withSquareRange(new Range(0, problemSize - 1))
                        .withSteps(problemSize).build());
                plot.setVisible(true);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}