package com.agh.iet.komplastech.solver.problem.terrain;

import Jama.Matrix;
import Jama.SingularValueDecomposition;
import com.agh.iet.komplastech.solver.*;
import com.agh.iet.komplastech.solver.problem.IterativeProblem;
import com.agh.iet.komplastech.solver.problem.NoopIterativeProblem;
import com.agh.iet.komplastech.solver.problem.ProblemManager;
import com.agh.iet.komplastech.solver.results.CsvPrinter;
import com.agh.iet.komplastech.solver.results.visualization.ResultsSnapshot;
import com.agh.iet.komplastech.solver.results.visualization.SolutionAsBitmapSnapshot;
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

import java.util.ArrayList;
import java.util.List;

import static com.agh.iet.komplastech.solver.support.MatrixUtils.withPadding;
import static com.agh.iet.komplastech.solver.support.MatrixUtils.withoutPadding;

public class TerrainManager implements ProblemManager {

    @Parameter(names = {"--terrain-file"})
    private String terrainFile;

    @Parameter(names = {"--terrain-scale"})
    private int scale = 1; // 100

    @Parameter(names = {"--terrain-x-offset"})
    private double xOffset = 0; // 560000;

    @Parameter(names = {"--terrain-y-offset"})
    private double yOffset = 0; //180000;

    @Parameter(names = {"--ranks", "-r"})
    private List<Integer> ranks = new ArrayList<>();

    private final Mesh mesh;

    private final SolverFactory solverFactory;

    private TerrainStorage inputTerrain;

    private Solution terrainSolution;

    private SingularValueDecomposition svd;


    public TerrainManager(Mesh mesh, SolverFactory solverFactory) {
        this.mesh = mesh;
        this.solverFactory = solverFactory;
    }

    @Override
    public SolutionFactory getSolutionFactory() {
        return solution -> new IntermediateSolution(mesh, solution.getRhs());
    }

    @Override
    public IterativeProblem getProblem() {
        return new NoopIterativeProblem(); // new TerrainProblem(newArrayList(1));
    }

    @Override
    public void displayResults(SolutionSeries solutionSeries) {
//        displayOriginalSolution();
        displayOriginalSolutionBitmap();
        displayRankApproximationsBitmaps();
//        displayMaxRankApproximation();

//        TimeLapseViewer timeLapseViewer = new TimeLapseViewer(solutionSeries);
//        timeLapseViewer.setVisible(true);
    }

    private void displayOriginalSolution() {
        ResultsSnapshot terrainView = new ResultsSnapshot("Original solution", terrainSolution);
        terrainView.setVisible(true);
    }

    private void displayOriginalSolutionBitmap() {
        SolutionAsBitmapSnapshot modelBitmap = new SolutionAsBitmapSnapshot("Original model solution", terrainSolution);
        modelBitmap.setVisible(true);
    }

    private void displayRankApproximationsBitmaps() {
        ranks.forEach(rank -> {
            final Solution svdApproximation = getSvdRankedSolution(rank);
            final double error = svdApproximation.squaredDifference(terrainSolution);
            SolutionAsBitmapSnapshot svdBitmap = new SolutionAsBitmapSnapshot(String.format("SVD rank %d approximation. Error: %s", rank, error), svdApproximation);
            svdBitmap.setVisible(true);
        });
    }

    private void displayMaxRankApproximation() {
        final int maxRank = ranks.stream().mapToInt(x -> x).max().getAsInt();
        final Solution maxRankApproximation = getSvdRankedSolution(maxRank);
        final double error = maxRankApproximation.squaredDifference(terrainSolution);
        ResultsSnapshot approxViewer = new ResultsSnapshot(String.format("SVD rank %d approximation. Error: %s", maxRank, error), maxRankApproximation);
        approxViewer.setVisible(true);
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
        MapTerrainStorage outputTerrain = new MapTerrainStorage(); // fast but does not work properly at the ends
//        KdTreeTerrainStorage outputTerrain = new KdTreeTerrainStorage(); // fast but does not work properly at the ends

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
        svd = new SingularValueDecomposition(new Matrix(
                withoutPadding(terrainSolution.getRhs())
        ));
    }

    private Solution getSvdRankedSolution(int rank) {
        Matrix fullApproximation = svd.getU().times(svd.getS()).times(svd.getV().transpose());

        double[] singluarValues = svd.getSingularValues();

        final Matrix rankedMatrix = new Matrix(fullApproximation.getRowDimension(), fullApproximation.getColumnDimension());
        for (int i = 0; i < rank; i++) {
            rankedMatrix.set(i, i, singluarValues[i]);
        }

        Matrix mat = svd.getU().times(rankedMatrix).times(svd.getV().transpose());
        return new IntermediateSolution(mesh, withPadding(mat.getArray()));
    }

    private TerrainStorage createTerrainInput() {
        if (terrainFile != null) {
            return FileTerrainStorage.builder().inFilePath(terrainFile).build();
        } else {
            return new MapTerrainStorage(FunctionTerrainBuilder.get()
                    .withMesh(mesh)
                    .withFunction((x, y) -> (double) y)
                    .build());
        }
    }

}
