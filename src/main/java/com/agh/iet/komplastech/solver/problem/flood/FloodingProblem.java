package com.agh.iet.komplastech.solver.problem.flood;

import com.agh.iet.komplastech.solver.Solution;
import com.agh.iet.komplastech.solver.problem.NonStationaryProblem;
import com.agh.iet.komplastech.solver.support.Mesh;

public class FloodingProblem extends NonStationaryProblem {

    private Mesh mesh;
    private int finalProblemSize;

    public FloodingProblem(double delta, Mesh mesh, int finalProblemSize) {
        super(delta);
        this.mesh = mesh;
        this.finalProblemSize = finalProblemSize;
    }

    @Override
    protected double getInitialValue(double x, double y) {
        double dist = (x - mesh.getCenterX()) * (x - mesh.getCenterX())
                + (y - mesh.getCenterY()) * (y - mesh.getCenterY());

        return dist < finalProblemSize ? finalProblemSize - dist : 0;
    }

    @Override
    protected double getValueAtTime(double x, double y, Solution currentSolution, double delta) {
        double value = currentSolution.getValue(x, y);
        return value + delta * currentSolution.getModifiedValue(x, y);
    }

}
