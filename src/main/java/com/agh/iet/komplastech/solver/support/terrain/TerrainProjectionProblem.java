package com.agh.iet.komplastech.solver.support.terrain;

import com.agh.iet.komplastech.solver.problem.Problem;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TerrainProjectionProblem implements Problem {

    private TerrainPointFinder terrainPointFinder;

    @Override
    public double getValue(double x, double y) {
        return terrainPointFinder.get(x - 1, y - 1).z;
    } // todo translate in inside the finder

}
