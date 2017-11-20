package com.agh.iet.komplastech.solver.problem.flood.terrain;

import com.agh.iet.komplastech.solver.problem.Problem;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TerrainProjectionProblem implements Problem {

    private TerrainPointFinder terrainPointFinder;

    @Override
    public double getValue(double x, double y) {
        return 50 * (x + y); //terrainPointFinder.get(x, y).z;
    }

}
