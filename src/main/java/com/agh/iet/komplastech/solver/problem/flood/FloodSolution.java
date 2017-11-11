package com.agh.iet.komplastech.solver.problem.flood;

import com.agh.iet.komplastech.solver.Solution;
import com.agh.iet.komplastech.solver.support.Mesh;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class FloodSolution extends Solution {

    private double meanValue;
    private double[][] terrain;

    public FloodSolution(Mesh mesh, double[][] rhs, double[][] terrain) {
        super(mesh, rhs);
        this.terrain = terrain;
        meanValue = computeMean();
    }

    @Override
    public final double getModifiedValue(double x, double y) {
        int ielemx = (int) (x / (mesh.getDx())) + 1;
        int ielemy = (int) (y / (mesh.getDy())) + 1;
        double localx = x - (mesh.getDx()) * (ielemx - 1);
        double localy = y - (mesh.getDy()) * (ielemy - 1);
        double solution = 0.0;

        double K = 0.0;

        // Ut
        K += b1.getValue(localx) * mRHS[ielemx][ielemy] - b1.getValue(localx) * terrain[ielemx][ielemy];
        K += b1.getValue(localx) * mRHS[ielemx][ielemy + 1] - b1.getValue(localx) * terrain[ielemx][ielemy + 1];
        K += b1.getValue(localx) * mRHS[ielemx][ielemy + 2] - b1.getValue(localx) * terrain[ielemx][ielemy + 2];
        K += b2.getValue(localx) * mRHS[ielemx + 1][ielemy] - b2.getValue(localx) * terrain[ielemx + 1][ielemy];
        K += b2.getValue(localx) * mRHS[ielemx + 1][ielemy + 1] - b2.getValue(localx) * terrain[ielemx + 1][ielemy + 1];
        K += b2.getValue(localx) * mRHS[ielemx + 1][ielemy + 2] - b2.getValue(localx) * terrain[ielemx + 1][ielemy + 2];
        K += b3.getValue(localx) * mRHS[ielemx + 2][ielemy] - b3.getValue(localx) * terrain[ielemx + 2][ielemy];
        K += b3.getValue(localx) * mRHS[ielemx + 2][ielemy + 1] - b3.getValue(localx) * terrain[ielemx + 2][ielemy + 1];
        K += b3.getValue(localx) * mRHS[ielemx + 2][ielemy + 2] - b3.getValue(localx) * terrain[ielemx + 2][ielemy + 2];

        if(K > 0) {
            K = Math.sqrt(K);
        } else {
            K = 0;
        }

        double X = K / meanValue;

        solution += b1.getFirstDerivativeValueAt(localx) * b1.getFirstDerivativeValueAt(localy) * mRHS[ielemx][ielemy];
        solution += b1.getFirstDerivativeValueAt(localx) * b2.getFirstDerivativeValueAt(localy) * mRHS[ielemx][ielemy + 1];
        solution += b1.getFirstDerivativeValueAt(localx) * b3.getFirstDerivativeValueAt(localy) * mRHS[ielemx][ielemy + 2];
        solution += b2.getFirstDerivativeValueAt(localx) * b1.getFirstDerivativeValueAt(localy) * mRHS[ielemx + 1][ielemy];
        solution += b2.getFirstDerivativeValueAt(localx) * b2.getFirstDerivativeValueAt(localy) * mRHS[ielemx + 1][ielemy + 1];
        solution += b2.getFirstDerivativeValueAt(localx) * b3.getFirstDerivativeValueAt(localy) * mRHS[ielemx + 1][ielemy + 2];
        solution += b3.getFirstDerivativeValueAt(localx) * b1.getFirstDerivativeValueAt(localy) * mRHS[ielemx + 2][ielemy];
        solution += b3.getFirstDerivativeValueAt(localx) * b2.getFirstDerivativeValueAt(localy) * mRHS[ielemx + 2][ielemy + 1];
        solution += b3.getFirstDerivativeValueAt(localx) * b3.getFirstDerivativeValueAt(localy) * mRHS[ielemx + 2][ielemy + 2];

        solution *= X;
        return solution;
    }

    private double computeMean() {
        BigDecimal total = BigDecimal.ZERO;
        final int yCount = mRHS.length;
        final int xCount = mRHS[1].length;
        for (int y = 1; y < yCount; y++) {
            for (int x = 1; x < xCount; x++) {
                total = total.add(new BigDecimal(mRHS[y][x]));
            }
        }
        return total.divide(new BigDecimal(xCount * yCount), RoundingMode.HALF_EVEN).doubleValue();
    }

}
