package com.agh.iet.komplastech.solver.problem.terrain;

import com.agh.iet.komplastech.solver.Solution;
import com.agh.iet.komplastech.solver.problem.Problem;
import com.agh.iet.komplastech.solver.problem.VectorIterativeProblem;

import java.util.List;

public class TerrainProblem extends VectorIterativeProblem<Solution> {

    public TerrainProblem(List<Solution> solutions) {
        super(solutions);
    }

    @Override
    protected Problem getProblemForValue(Solution solution) {
        return solution::getValue;
    }

}
