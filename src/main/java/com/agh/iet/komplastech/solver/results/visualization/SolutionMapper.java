package com.agh.iet.komplastech.solver.results.visualization;

import com.agh.iet.komplastech.solver.Solution;
import org.jzy3d.plot3d.builder.Mapper;

public class SolutionMapper extends Mapper {

    private final Solution solution;

    public SolutionMapper(Solution solution) {
        this.solution = solution;
    }

    @Override
    public double f(double x, double y) {
        return solution.getValue(x, y);
    }

    public static SolutionMapper fromSolution(Solution solution) {
        return new SolutionMapper(solution);
    }

}
