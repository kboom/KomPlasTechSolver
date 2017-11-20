package com.agh.iet.komplastech.solver.problem.heat;

import com.agh.iet.komplastech.solver.Solution;
import com.agh.iet.komplastech.solver.SolutionFactory;
import com.agh.iet.komplastech.solver.SolutionSeries;
import com.agh.iet.komplastech.solver.problem.IterativeProblem;
import com.agh.iet.komplastech.solver.problem.ProblemManager;
import com.agh.iet.komplastech.solver.results.CsvPrinter;
import com.agh.iet.komplastech.solver.results.visualization.TimeLapseViewer;
import com.agh.iet.komplastech.solver.support.Mesh;
import com.beust.jcommander.Parameter;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class HeatManager implements ProblemManager {

    private final Mesh mesh;
    private final int problemSize;
    private final double delta;

    @Parameter(names = {"--steps", "-o"})
    private int steps = 100;

    @Override
    public IterativeProblem getProblem() {
        return new HeatTransferProblem(delta, mesh, problemSize, steps);
    }

    @Override
    public SolutionFactory getSolutionFactory() {
        return solution -> new HeatSolution(mesh, solution.getRhs());
    }

    @Override
    public void displayResults(SolutionSeries solutionSeries) {
        TimeLapseViewer timeLapseViewer = new TimeLapseViewer(solutionSeries);
        timeLapseViewer.setVisible(true);
    }

    @Override
    public void logResults(SolutionSeries solutionSeries) {
        Solution finalSolution = solutionSeries.getFinalSolution();
        CsvPrinter csvPrinter = new CsvPrinter();
        System.out.println(csvPrinter.convertToCsv(finalSolution.getSolutionGrid()));
    }

    @Override
    public void setUp() {

    }

    @Override
    public void tearDown() {

    }

}
