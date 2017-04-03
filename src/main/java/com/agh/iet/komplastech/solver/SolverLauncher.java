package com.agh.iet.komplastech.solver;

import com.agh.iet.komplastech.solver.logger.ConsoleSolutionLogger;
import com.agh.iet.komplastech.solver.logger.NoopSolutionLogger;
import com.agh.iet.komplastech.solver.problem.HeatTransferProblem;
import com.agh.iet.komplastech.solver.results.CsvPrinter;
import com.agh.iet.komplastech.solver.results.visualization.TimeLapseViewer;
import com.agh.iet.komplastech.solver.storage.HazelcastObjectStore;
import com.agh.iet.komplastech.solver.storage.ObjectStore;
import com.agh.iet.komplastech.solver.support.*;
import com.beust.jcommander.Parameter;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import org.apache.log4j.Logger;

import static com.agh.iet.komplastech.solver.support.Mesh.aMesh;
import static java.lang.String.format;

class SolverLauncher {

    private static final Logger log = Logger.getLogger(SolverLauncher.class);

    @Parameter(names = {"--log", "-l"})
    private boolean isLogging = false;

    @Parameter(names = {"--plot", "-p"})
    private boolean isPlotting = false;

    @Parameter(names = {"--problem-size", "-s"})
    private int problemSize = 24;

    @Parameter(names = {"--delta", "-d"})
    private double delta = 0.1;

    @Parameter(names = {"--steps", "-o"})
    private int steps = 100;

    @Parameter(names = {"--batches", "-b"})
    private int maxBatchSize = 10;

    @Parameter(names = {"--regionHeight", "-r"})
    private int regionHeight = 6;

    void launch() {
        log.info(format("Problem size (%d), Steps (%d), Batch size (%d)",
                problemSize, steps, maxBatchSize));

        ComputeConfig computeConfig = ComputeConfig.aComputeConfig()
                .withRegionHeight(regionHeight)
                .build();

        VertexRegionMapper vertexRegionMapper = new VertexRegionMapper(computeConfig);
        HazelcastInstance hazelcastInstance = HazelcastClient.newHazelcastClient();
        ObjectStore objectStore = new HazelcastObjectStore(hazelcastInstance, vertexRegionMapper);
        ProductionExecutorFactory productionExecutorFactory = new ProductionExecutorFactory(
                hazelcastInstance, vertexRegionMapper, maxBatchSize);
        VertexMap vertexMap = new HazelcastVertexMap(hazelcastInstance.getMap("vertices"), vertexRegionMapper);

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
                vertexRegionMapper,
                isLogging ? new ConsoleSolutionLogger(mesh, vertexMap) : new NoopSolutionLogger(),
                objectStore,
                timeLogger
        );

        try {
            NonStationarySolver nonStationarySolver =
                    new NonStationarySolver(steps, delta, problemSolver, mesh);


            SolutionsInTime solutionsInTime = nonStationarySolver.solveInTime(
                    new HeatTransferProblem(delta, mesh, problemSize)
//                    new ConstantLinearProblem(delta)
            );


            log.info(format("%d,%d,%d,%d",
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

            objectStore.clearAll();
            hazelcastInstance.shutdown();

        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

}
