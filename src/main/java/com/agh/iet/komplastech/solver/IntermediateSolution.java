package com.agh.iet.komplastech.solver;

import com.agh.iet.komplastech.solver.support.Mesh;

public final class IntermediateSolution extends Solution {

    public IntermediateSolution(Mesh mesh, double[][] rhs) {
        super(mesh, rhs);
    }

    @Override
    public double getModifiedValue(double x, double y) {
        return getValue(x, y);
    }

}
