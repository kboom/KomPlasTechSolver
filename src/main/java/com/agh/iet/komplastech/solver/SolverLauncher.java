package com.agh.iet.komplastech.solver;

import com.agh.iet.komplastech.solver.logger.ConsoleSolutionLogger;
import com.agh.iet.komplastech.solver.logger.NoopSolutionLogger;
import com.agh.iet.komplastech.solver.logger.process.ConsoleProcessLogger;
import com.agh.iet.komplastech.solver.logger.process.NoopProcessLogger;
import com.agh.iet.komplastech.solver.logger.process.ProcessLogger;
import com.agh.iet.komplastech.solver.problem.ConstantLinearProblem;
import com.agh.iet.komplastech.solver.problem.ConstantOneProblem;
import com.agh.iet.komplastech.solver.problem.HeatTransferProblem;
import com.agh.iet.komplastech.solver.problem.NonStationaryProblem;
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

    @Parameter(names = {"--log-solution", "-l"})
    private boolean isLoggingSolution = false;

    @Parameter(names = {"--solution-hashes", "-h"})
    private boolean isPrintingSolutionHashes = false;

    @Parameter(names = {"--log-process", "-x"})
    private boolean isLoggingProcess = false;

    @Parameter(names = {"--plot", "-p"})
    private boolean isPlotting = false;

    @Parameter(names = {"--problem-size", "-s"})
    private int problemSize = 768;

    @Parameter(names = {"--delta", "-d"})
    private double delta = 0.0000001;

    @Parameter(names = {"--steps", "-o"})
    private int steps = 100;

    @Parameter(names = {"--batch-ratio"})
    private int batchRatio = 4;

    @Parameter(names = {"--max-batch-size"})
    private int maxBatchSize = 10;

    @Parameter(names = {"--region-height"})
    private int regionHeight = 6;

    @Parameter(names = {"--max-job-count"})
    private int maxJobCount = 100;

    @Parameter(names = {"--max-solution-batch-size"})
    private int maxSolutionBatchSize = 1000;

    @Parameter(names = {"--problem"})
    private String solvedProblem = "heat";

    void launch() {
        ComputeConfig computeConfig = ComputeConfig.aComputeConfig()
                .withRegionHeight(regionHeight)
                .withMaxBatchSize(maxBatchSize)
                .withBatchRatio(batchRatio)
                .withMaxSolutionBatchSize(maxSolutionBatchSize)
                .withMaxJobCount(maxJobCount)
                .build();


        Mesh mesh = aMesh()
                .withElementsX(problemSize)
                .withElementsY(problemSize)
                .withResolutionX(problemSize)
                .withResolutionY(problemSize)
                .withOrder(2).build();

        VertexRegionMapper vertexRegionMapper = new VertexRegionMapper(mesh, computeConfig);
        HazelcastInstance hazelcastInstance = HazelcastClient.newHazelcastClient();

        log.info(format("\n\n--------- Problem size (%d), Steps (%d), Max batch size (%d), Batch ratio (%d), Region height (%d), Member count (%d)",
                problemSize, steps, maxBatchSize, batchRatio, regionHeight, hazelcastInstance.getCluster().getMembers().size()));

        final ProcessLogger processLogger = isLoggingProcess ? new ConsoleProcessLogger() : new NoopProcessLogger();

        ObjectStore objectStore = new HazelcastObjectStore(hazelcastInstance);
        ProductionExecutorFactory productionExecutorFactory = new ProductionExecutorFactory(
                hazelcastInstance, vertexRegionMapper, computeConfig, processLogger);

        VertexMap vertexMap = new HazelcastVertexMap(
                hazelcastInstance.getExecutorService("general"),
                hazelcastInstance.getMap("vertices"),
                vertexRegionMapper,
                computeConfig
        );

        TimeLogger timeLogger = new TimeLogger();

        HazelcastFacade hazelcastFacade = new HazelcastFacade(hazelcastInstance);

        TwoDimensionalProblemSolver problemSolver = new TwoDimensionalProblemSolver(
                hazelcastFacade,
                productionExecutorFactory,
                new PartialSolutionManager(mesh, hazelcastInstance),
                mesh,
                computeConfig,
                isLoggingSolution ? new ConsoleSolutionLogger(mesh, vertexMap) : new NoopSolutionLogger(),
                processLogger,
                objectStore,
                timeLogger
        );

        try {
            NonStationarySolver nonStationarySolver =
                    new NonStationarySolver(steps, delta, problemSolver, mesh, isPrintingSolutionHashes);


            NonStationaryProblem nonStationaryProblem;
            switch (solvedProblem) {
                case "heat":
                    nonStationaryProblem = new HeatTransferProblem(delta, mesh, problemSize);
                    break;
                case "linear":
                    nonStationaryProblem = new ConstantLinearProblem(delta);
                    break;
                case "one":
                    nonStationaryProblem = new ConstantOneProblem(delta);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown problem type " + solvedProblem);
            }

            nonStationarySolver.solveInTime(nonStationaryProblem);

            log.info(format("Solution times: Creation (%d), Initialization (%d), Factorization (%d), " +
                            "Backwards Substitution (%d), Solution reading (%d), First stage (%d), Second Stage (%d), Total (%d)",
                    timeLogger.getTotalCreationMs(),
                    timeLogger.getTotalInitializationMs(),
                    timeLogger.getTotalFactorizationMs(),
                    timeLogger.getTotalBackwardSubstitutionMs(),
                    timeLogger.getTotalSolutionReadingMs(),
                    timeLogger.getFirstStageTimeMs(),
                    timeLogger.getSecondStageTimeMs(),
                    timeLogger.getTotalSolutionMs()
            ));


            // Solution solution = solutionsInTime.getFinalSolution();

            // if (isLoggingSolution) {
            //     CsvPrinter csvPrinter = new CsvPrinter();
            //     System.out.println(csvPrinter.convertToCsv(solution.getSolutionGrid()));
            // }

            // if (isPlotting) {
            //     TimeLapseViewer timeLapseViewer = new TimeLapseViewer(solutionsInTime);
            //     timeLapseViewer.setVisible(true);
            // }

            objectStore.clearVertices();
            hazelcastInstance.shutdown();

        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

}
