package com.agh.iet.komplastech.solver.problem.flood;

import com.agh.iet.komplastech.solver.*;
import com.agh.iet.komplastech.solver.problem.NonStationaryProblem;
import com.agh.iet.komplastech.solver.problem.ProblemManager;
import com.agh.iet.komplastech.solver.problem.flood.terrain.*;
import com.agh.iet.komplastech.solver.problem.flood.terrain.processors.AdjustmentTerrainProcessor;
import com.agh.iet.komplastech.solver.problem.flood.terrain.processors.ChainedTerrainProcessor;
import com.agh.iet.komplastech.solver.problem.flood.terrain.processors.ToClosestTerrainProcessor;
import com.agh.iet.komplastech.solver.problem.flood.terrain.support.Point2D;
import com.agh.iet.komplastech.solver.results.CsvPrinter;
import com.agh.iet.komplastech.solver.results.visualization.ResultsSnapshot;
import com.agh.iet.komplastech.solver.results.visualization.TimeLapseViewer;
import com.agh.iet.komplastech.solver.support.Mesh;
import com.beust.jcommander.Parameter;

public class FloodManager implements ProblemManager {

    @Parameter(names={"--terrain-file"})
    private String terrainFile;

    @Parameter(names={"--terrain-scale"})
    private int scale = 1; //1000;

    @Parameter(names={"--terrain-x-offset"})
    private double xOffset = 0; //506000;

    @Parameter(names={"--terrain-y-offset"})
    private double yOffset = 0; //150000;

    @Parameter(names={"--delta", "-d"})
    private double delta = 0.001;

    private final Mesh mesh;

    private final SolverFactory solverFactory;

    private TerrainStorage inputTerrain;

    private Solution terrainSolution;

    public FloodManager(Mesh mesh, SolverFactory solverFactory) {
        this.mesh = mesh;
        this.solverFactory = solverFactory;
    }

    @Override
    public SolutionFactory getSolutionFactory() {
        return solution -> new FloodSolution(mesh, solution.getRhs(), terrainSolution.getRhs());
    }

    @Override
    public NonStationaryProblem getProblem() {
        return new FloodingProblem(delta, terrainSolution);
    }

    @Override
    public void displayResults(SolutionsInTime solutionsInTime) {
        ResultsSnapshot terrainView = new ResultsSnapshot(terrainSolution);
        terrainView.setVisible(true);

        TimeLapseViewer timeLapseViewer = new TimeLapseViewer(solutionsInTime);
        timeLapseViewer.setVisible(true);
    }

    @Override
    public void logResults(SolutionsInTime solutionsInTime) {
        Solution finalSolution = solutionsInTime.getFinalSolution();
        CsvPrinter csvPrinter = new CsvPrinter();
        System.out.println(csvPrinter.convertToCsv(finalSolution.getSolutionGrid()));
    }

    @Override
    public void setUp() {
        inputTerrain = createTerrainInput();
        MapTerrainStorage outputTerrain = new MapTerrainStorage();

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

        Solver solver = solverFactory.createSolver(solution -> solution);
        terrainSolution = solver.solveProblem(new TerrainProjectionProblem(outputTerrain));
    }

    @Override
    public void tearDown() {

    }

    private TerrainStorage createTerrainInput() {
        if(terrainFile != null) {
            return FileTerrainStorage.builder().inFilePath(terrainFile).build();
        } else {
            return new MapTerrainStorage(FunctionTerrainBuilder.get().withMesh(mesh).withFunction().build());
        }
    }

}
