package com.agh.iet.komplastech.solver.problem.flood;

import com.agh.iet.komplastech.solver.Solution;
import com.agh.iet.komplastech.solver.support.Mesh;

import static com.agh.iet.komplastech.solver.support.MatrixUtils.computeMeanOf;

public class FloodSolution extends Solution {

    private double meanValue;
    private double[][] terrain;

    public FloodSolution(Mesh mesh, double[][] rhs, double[][] terrain) {
        super(mesh, rhs);
        this.terrain = terrain;
        meanValue = computeMeanOf(rhs);
    }

    @Override
    public final double getModifiedValue(double x, double y) {
        int ielemx = (int) (x / (mesh.getDx())) + 1;
        int ielemy = (int) (y / (mesh.getDy())) + 1;
        double localx = x - (mesh.getDx()) * (ielemx - 1);
        double localy = y - (mesh.getDy()) * (ielemy - 1);

        double Ut = getValue(mRHS, x, y);
        double Z = getValue(terrain, x, y);

        double diffUtZ = Math.max(Ut - Z, 0);

        double K = Math.pow(diffUtZ, 5.0 / 3.0) / meanValue;

        return K * (b1.getSecondDerivativeValueAt(localx) * b1.getValue(localy) * mRHS[ielemx][ielemy]
                + b1.getSecondDerivativeValueAt(localx) * b2.getValue(localy) * mRHS[ielemx][ielemy + 1]
                + b1.getSecondDerivativeValueAt(localx) * b3.getValue(localy) * mRHS[ielemx][ielemy + 2]
                + b2.getSecondDerivativeValueAt(localx) * b1.getValue(localy) * mRHS[ielemx + 1][ielemy]
                + b2.getSecondDerivativeValueAt(localx) * b2.getValue(localy) * mRHS[ielemx + 1][ielemy + 1]
                + b2.getSecondDerivativeValueAt(localx) * b3.getValue(localy) * mRHS[ielemx + 1][ielemy + 2]
                + b3.getSecondDerivativeValueAt(localx) * b1.getValue(localy) * mRHS[ielemx + 2][ielemy]
                + b3.getSecondDerivativeValueAt(localx) * b2.getValue(localy) * mRHS[ielemx + 2][ielemy + 1]
                + b3.getSecondDerivativeValueAt(localx) * b3.getValue(localy) * mRHS[ielemx + 2][ielemy + 2]);
    }

}
