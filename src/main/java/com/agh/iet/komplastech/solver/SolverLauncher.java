package com.agh.iet.komplastech.solver;

import com.agh.iet.komplastech.solver.logger.ConsoleSolutionLogger;
import com.agh.iet.komplastech.solver.logger.NoopSolutionLogger;
import com.agh.iet.komplastech.solver.problem.HeatTransferProblem;
import com.agh.iet.komplastech.solver.problem.NonStationaryProblem;
import com.agh.iet.komplastech.solver.results.CsvPrinter;
import com.agh.iet.komplastech.solver.results.visualization.TimeLapseViewer;
import com.agh.iet.komplastech.solver.storage.HazelcastObjectStore;
import com.agh.iet.komplastech.solver.storage.ObjectStore;
import com.agh.iet.komplastech.solver.support.Mesh;
import com.beust.jcommander.Parameter;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;

import static com.agh.iet.komplastech.solver.support.Mesh.aMesh;

class SolverLauncher {

    @Parameter(names = {"--log", "-l"})
    private boolean isLogging = false;

    @Parameter(names = {"--plot", "-p"})
    private boolean isPlotting = true;

    @Parameter(names = {"--problem-size", "-s"})
    private int problemSize = 12;

    @Parameter(names = {"--max-threads", "-t"})
    private int maxThreads = 12;

    @Parameter(names = {"--delta", "-d"})
    private double delta = 0.001;

    @Parameter(names = {"--steps", "-o"})
    private int steps = 100;

    @Parameter(names = {"--cloud"})
    private boolean isCloud = true;

    void launch() {
        HazelcastInstance hazelcastInstance = HazelcastClient.newHazelcastClient();
        ObjectStore objectStore = new HazelcastObjectStore(hazelcastInstance);
        ProductionExecutorFactory productionExecutorFactory = new ProductionExecutorFactory(
                hazelcastInstance.getExecutorService("productionExecutor"));

        hazelcastInstance.getMap("vertices").clear();


        TimeLogger timeLogger = new TimeLogger();

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
                objectStore,
                timeLogger
        );

        try {
            NonStationarySolver nonStationarySolver =
                    new NonStationarySolver(steps, delta, problemSolver, mesh);


            SolutionsInTime solutionsInTime = nonStationarySolver.solveInTime(
                    new HeatTransferProblem(delta, mesh, problemSize)
            );


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
