package com.agh.iet.komplastech.solver.problem.terrain;

import Jama.Matrix;
import Jama.SingularValueDecomposition;
import com.agh.iet.komplastech.solver.*;
import com.agh.iet.komplastech.solver.problem.IterativeProblem;
import com.agh.iet.komplastech.solver.problem.ProblemManager;
import com.agh.iet.komplastech.solver.problem.flood.FloodSolution;
import com.agh.iet.komplastech.solver.results.CsvPrinter;
import com.agh.iet.komplastech.solver.results.visualization.ResultsSnapshot;
import com.agh.iet.komplastech.solver.results.visualization.TimeLapseViewer;
import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.support.terrain.FunctionTerrainBuilder;
import com.agh.iet.komplastech.solver.support.terrain.Terraformer;
import com.agh.iet.komplastech.solver.support.terrain.TerrainProjectionProblem;
import com.agh.iet.komplastech.solver.support.terrain.processors.AdjustmentTerrainProcessor;
import com.agh.iet.komplastech.solver.support.terrain.processors.ChainedTerrainProcessor;
import com.agh.iet.komplastech.solver.support.terrain.processors.ToClosestTerrainProcessor;
import com.agh.iet.komplastech.solver.support.terrain.storage.FileTerrainStorage;
import com.agh.iet.komplastech.solver.support.terrain.storage.MapTerrainStorage;
import com.agh.iet.komplastech.solver.support.terrain.storage.TerrainStorage;
import com.agh.iet.komplastech.solver.support.terrain.support.Point2D;
import com.beust.jcommander.Parameter;

import static com.agh.iet.komplastech.solver.support.MatrixPaddingTranslator.withPadding;
import static com.agh.iet.komplastech.solver.support.MatrixPaddingTranslator.withoutPadding;
import static com.beust.jcommander.internal.Lists.newArrayList;

public class TerrainManager implements ProblemManager {

    @Parameter(names = {"--terrain-file"})
    private String terrainFile;

    @Parameter(names = {"--terrain-scale"})
    private int scale = 1; //1000;

    @Parameter(names = {"--terrain-x-offset"})
    private double xOffset = 0; //506000;

    @Parameter(names = {"--terrain-y-offset"})
    private double yOffset = 0; //150000;

    @Parameter(names = {"--delta", "-d"})
    private double delta = 0.001;

    @Parameter(names = {"--rank", "-r"})
    private double rank = 10;

    private final Mesh mesh;

    private final SolverFactory solverFactory;

    private TerrainStorage inputTerrain;

    private Solution terrainSolution;

    private Solution svdApproximation;

    public TerrainManager(Mesh mesh, SolverFactory solverFactory) {
        this.mesh = mesh;
        this.solverFactory = solverFactory;
    }

    @Override
    public SolutionFactory getSolutionFactory() {
        return solution -> new FloodSolution(mesh, solution.getRhs(), terrainSolution.getRhs());
    }

    @Override
    public IterativeProblem getProblem() {
        return new TerrainProblem(newArrayList(1));
    }

    @Override
    public void displayResults(SolutionSeries solutionSeries) {
        ResultsSnapshot terrainView = new ResultsSnapshot(terrainSolution);
        terrainView.setVisible(true);

        ResultsSnapshot approxViewer = new ResultsSnapshot(svdApproximation);
        approxViewer.setVisible(true);

//        TimeLapseViewer timeLapseViewer = new TimeLapseViewer(solutionSeries);
//        timeLapseViewer.setVisible(true);
    }

    @Override
    public void logResults(SolutionSeries solutionSeries) {
        Solution finalSolution = solutionSeries.getFinalSolution();
        CsvPrinter csvPrinter = new CsvPrinter();
        System.out.println("------------------- TERRAIN SOLUTION --------------------");
        System.out.println(csvPrinter.convertToCsv(terrainSolution.getSolutionGrid()));
        System.out.println("------------------- FINAL SIM SOLUTION --------------------");
        System.out.println(csvPrinter.convertToCsv(finalSolution.getSolutionGrid()));
    }

    @Override
    public void setUp() {
        computeTerrainProjection();
        computeSvd();
    }

    @Override
    public void tearDown() {

    }

    private void computeTerrainProjection() {
        inputTerrain = createTerrainInput();
        MapTerrainStorage outputTerrain = new MapTerrainStorage();

        Terraformer.builder()
                .inputStorage(inputTerrain)
                .outputStorage(outputTerrain)
                .terrainProcessor(
                        ChainedTerrainProcessor.startingFrom(AdjustmentTerrainProcessor.builder().center(new Point2D(xOffset, yOffset)).scale(scale).build())
                                .withNext(new ToClosestTerrainProcessor())
                                .withNext(AdjustmentTerrainProcessor.builder().center(new Point2D(-xOffset, -yOffset)).scale(1d / scale).build())
                )
                .build()
                .terraform(mesh);

        Solver solver = solverFactory.createSolver(solution -> solution);
        terrainSolution = solver.solveProblem(new TerrainProjectionProblem(outputTerrain));
    }

    private void computeSvd() {
        final SingularValueDecomposition svd = new SingularValueDecomposition(new Matrix(
                withoutPadding(terrainSolution.getRhs())
        ));

        Matrix fullApproximation = svd.getU().times(svd.getS()).times(svd.getV().transpose());

        double[] singluarValues = svd.getSingularValues();

        final Matrix rankedMatrix = new Matrix(fullApproximation.getRowDimension(), fullApproximation.getColumnDimension());
        for (int i = 0; i < rank; i++) {
            rankedMatrix.set(i, i, singluarValues[i]);
        }

        Matrix mat = svd.getU().times(rankedMatrix).times(svd.getV().transpose());
        svdApproximation = new IntermediateSolution(mesh, withPadding(mat.getArray()));
    }

    private TerrainStorage createTerrainInput() {
        if (terrainFile != null) {
            return FileTerrainStorage.builder().inFilePath(terrainFile).build();
        } else {
            return new MapTerrainStorage(FunctionTerrainBuilder.get()
                    .withMesh(mesh)
                    .withFunction((x, y) -> (double) 10 * (x + y))
                    .build());
        }
    }

}
