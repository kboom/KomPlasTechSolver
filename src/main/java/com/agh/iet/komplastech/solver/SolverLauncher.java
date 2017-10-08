package com.agh.iet.komplastech.solver;

import com.agh.iet.komplastech.solver.execution.ProductionExecutorFactory;
import com.agh.iet.komplastech.solver.logger.ConsoleSolutionLogger;
import com.agh.iet.komplastech.solver.logger.NoopSolutionLogger;
import com.agh.iet.komplastech.solver.problem.ProblemFactory;
import com.agh.iet.komplastech.solver.problem.heat.HeatFactory;
import com.agh.iet.komplastech.solver.results.CsvPrinter;
import com.agh.iet.komplastech.solver.results.visualization.TimeLapseViewer;
import com.agh.iet.komplastech.solver.results.visualization.ResultsSnapshot;
import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.terrain.FileTerrainStorage;
import com.agh.iet.komplastech.solver.terrain.InMemoryTerrainStorage;
import com.agh.iet.komplastech.solver.terrain.Terraformer;
import com.agh.iet.komplastech.solver.terrain.TerrainProjectionProblem;
import com.agh.iet.komplastech.solver.terrain.processors.AdjustmentTerrainProcessor;
import com.agh.iet.komplastech.solver.terrain.processors.ChainedTerrainProcessor;
import com.agh.iet.komplastech.solver.terrain.processors.ToClosestTerrainProcessor;
import com.agh.iet.komplastech.solver.terrain.support.Point2D;
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
    private int steps = 10;

    @Parameter(names={"--terrain-file"})
    private String terrainFile;

    @Parameter(names={"--terrain-scale"})
    private int scale = 1000;

    @Parameter(names={"--terrain-x-offset"})
    private double xOffset = 506000;

    @Parameter(names={"--terrain-y-offset"})
    private double yOffset = 150000;

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


        FileTerrainStorage inputTerrain = FileTerrainStorage.builder().inFilePath(terrainFile).build();
        InMemoryTerrainStorage outputTerrain = new InMemoryTerrainStorage();

        Terraformer.builder()
                .inputStorage(inputTerrain)
                .outputStorage(outputTerrain)
                .terrainProcessor(
                        ChainedTerrainProcessor.startingFrom(AdjustmentTerrainProcessor.builder().center(new Point2D(xOffset, yOffset)).scale(scale).build())
                                .withNext(new ToClosestTerrainProcessor())
                                .withNext(AdjustmentTerrainProcessor.builder().center(new Point2D(-xOffset, -yOffset)).scale(1d/scale).build())
                )
                .build()
                .terraform(mesh);

        Solution terrainSolution = problemSolver.solveProblem(new TerrainProjectionProblem(outputTerrain));


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
                ResultsSnapshot timeLapseViewer = new ResultsSnapshot(terrainSolution);
                timeLapseViewer.setVisible(true);

                TimeLapseViewer timeLapseViewer = new TimeLapseViewer(solutionsInTime);
                timeLapseViewer.setVisible(true);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
