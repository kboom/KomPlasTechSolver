package com.agh.iet.komplastech.solver.problem.heat;

import com.agh.iet.komplastech.solver.Solution;
import com.agh.iet.komplastech.solver.SolutionFactory;
import com.agh.iet.komplastech.solver.SolutionsInTime;
import com.agh.iet.komplastech.solver.problem.NonStationaryProblem;
import com.agh.iet.komplastech.solver.problem.ProblemManager;
import com.agh.iet.komplastech.solver.results.CsvPrinter;
import com.agh.iet.komplastech.solver.results.visualization.TimeLapseViewer;
import com.agh.iet.komplastech.solver.support.Mesh;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class HeatManager implements ProblemManager {

    private final Mesh mesh;
    private final int problemSize;
    private final double delta;

    @Override
    public NonStationaryProblem getProblem() {
        return new HeatTransferProblem(delta, mesh, problemSize);
    }

    @Override
    public SolutionFactory getSolutionFactory() {
        return solution -> new HeatSolution(mesh, solution.getRhs());
    }

    @Override
    public void displayResults(SolutionsInTime solutionsInTime) {
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

    }

    @Override
    public void tearDown() {

    }

}
