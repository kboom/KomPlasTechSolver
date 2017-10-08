package com.agh.iet.komplastech.solver.results.visualization;

import com.agh.iet.komplastech.solver.Solution;
import lombok.AllArgsConstructor;
import org.jzy3d.plot3d.builder.Mapper;

@AllArgsConstructor
public class SolutionMapper extends Mapper {

    private Solution solution;

    @Override
    public double f(double v, double v1) {
        return solution.getValue(v, v1);
    }

}
