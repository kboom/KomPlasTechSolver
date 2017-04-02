package com.agh.iet.komplastech.solver.problem;

import com.agh.iet.komplastech.solver.Solution;
import com.agh.iet.komplastech.solver.support.Mesh;

public class HeatTransferProblem extends NonStationaryProblem {

    private final int problemSize;
    private final Mesh mesh;

    public HeatTransferProblem(double delta, Mesh mesh, int problemSize) {
        super(delta);
        this.mesh = mesh;
        this.problemSize = problemSize;
    }

    @Override
    protected double getInitialValue(double x, double y) {
        double dist = (x - mesh.getCenterX()) * (x - mesh.getCenterX())
                + (y - mesh.getCenterY()) * (y - mesh.getCenterY());

        return dist < problemSize ? problemSize - dist : 0;
    }

    @Override
    protected double getValueAtTime(double x, double y, Solution currentSolution, double delta) {
        double value = currentSolution.getValue(x, y);
        return value + delta * currentSolution.getLaplacian(x, y);
    }
}
