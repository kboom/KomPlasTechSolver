package com.agh.iet.komplastech.solver;

import com.agh.iet.komplastech.solver.execution.ProductionExecutorFactory;
import com.agh.iet.komplastech.solver.logger.ConsoleSolutionLogger;
import com.agh.iet.komplastech.solver.logger.NoopSolutionLogger;
import com.agh.iet.komplastech.solver.problem.ProblemManager;
import com.agh.iet.komplastech.solver.problem.flood.FloodManager;
import com.agh.iet.komplastech.solver.problem.heat.HeatManager;
import com.agh.iet.komplastech.solver.problem.terrain.TerrainManager;
import com.agh.iet.komplastech.solver.support.Mesh;
import com.beust.jcommander.Parameter;

import static com.agh.iet.komplastech.solver.Main.withInjectedProgramArguments;
import static com.agh.iet.komplastech.solver.support.Mesh.aMesh;

class SolverLauncher {

    @Parameter(names = {"--log", "-l"})
    private boolean isLogging = false;

    @Parameter(names = {"--problem"})
    private String problemType = "heat";

    @Parameter(names = {"--plot", "-p"})
    private boolean isPlotting = false;

    @Parameter(names = {"--problem-size", "-s"})
    private int problemSize = 48;

    @Parameter(names = {"--max-threads", "-t"})
    private int maxThreads = 12;

    @Parameter(names = {"--delta", "-d"})
    private double delta = 0.001;

    private SolverFactory solverFactory;

    private Mesh mesh;

    void launch() {
        ProductionExecutorFactory productionExecutorFactory = new ProductionExecutorFactory();
        TimeLogger timeLogger = new TimeLogger();

        productionExecutorFactory.setAvailableThreads(maxThreads);

        mesh = aMesh()
                .withElementsX(problemSize)
                .withElementsY(problemSize)
                .withResolutionX(problemSize)
                .withResolutionY(problemSize)
                .withOrder(2).build();

        solverFactory = new SolverFactory(
                mesh,
                isLogging ? new ConsoleSolutionLogger(mesh) : new NoopSolutionLogger(),
                timeLogger
        );

        launchProblem(problemManager -> {
            final Solver solver = solverFactory.createSolver(problemManager.getSolutionFactory());

            try {
                IterativeSolver iterativeSolver = new IterativeSolver(solver, mesh);


                SolutionSeries solutionSeries = iterativeSolver.solveIteratively(problemManager.getProblem());

                productionExecutorFactory.joinAll();

                System.out.print(String.format("%d,%d,%d,%d",
                        timeLogger.getTotalCreationMs(),
                        timeLogger.getTotalInitializationMs(),
                        timeLogger.getTotalFactorizationMs(),
                        timeLogger.getTotalSolutionMs()
                ));

                if (isLogging) {
                    problemManager.logResults(solutionSeries);
                }

                if (isPlotting) {
                    problemManager.displayResults(solutionSeries);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void launchProblem(ProblemLauncher problemLauncher) {
        ProblemManager problemManager = withInjectedProgramArguments(createProblemManager());
        problemManager.setUp();
        problemLauncher.launch(problemManager);
        problemManager.tearDown();
    }

    private ProblemManager createProblemManager() {
        switch (problemType) {
            case "heat":
                return createHeatProblemFactory();
            case "flood":
                return createFloodProblemFactory();
            case "terrain-svd":
                return createTerrainSvdProblemFactory();
            default:
                throw new IllegalStateException("Could not identify the problem");
        }
    }

    private ProblemManager createTerrainSvdProblemFactory() {
        return new TerrainManager(mesh, solverFactory);
    }

    private ProblemManager createFloodProblemFactory() {
        return new FloodManager(mesh, solverFactory);
    }

    private ProblemManager createHeatProblemFactory() {
        return HeatManager.builder()
                .delta(delta)
                .mesh(mesh)
                .problemSize(problemSize)
                .build();
    }

    private interface ProblemLauncher {

        void launch(ProblemManager problemManager);

    }

}
