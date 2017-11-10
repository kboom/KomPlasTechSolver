package com.agh.iet.komplastech.solver;

import com.agh.iet.komplastech.solver.splines.BSpline1;
import com.agh.iet.komplastech.solver.splines.BSpline2;
import com.agh.iet.komplastech.solver.splines.BSpline3;
import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.support.Point;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.agh.iet.komplastech.solver.support.Point.solutionPoint;
import static com.agh.iet.komplastech.solver.SolutionGrid.solutionGrid;

public class Solution {

    private static final BSpline1 b1 = new BSpline1();
    private static final BSpline2 b2 = new BSpline2();
    private static final BSpline3 b3 = new BSpline3();

    private Mesh mesh;
    private double[][] mRHS;
    private Double meanValue;

    public Solution(Mesh mesh, double[][] rhs) {
        this.mesh = mesh;
        mRHS = rhs;
    }

    public SolutionGrid getSolutionGrid() {
        double Dx = mesh.getDx();
        double Dy = mesh.getDy();
        double x = -Dx / 2;
        double y;
        SolutionGrid solutionGrid = solutionGrid();
        for (int i = 1; i <= mesh.getElementsX(); ++i) {
            x += Dx;
            y = -Dy / 2;
            for (int j = 1; j <= mesh.getElementsY(); ++j) {
                y += Dy;
                Point point = solutionPoint(x, y, getValue(x, y));
                solutionGrid.addPoint(point);
            }
        }
        return solutionGrid;
    }

    public double getValue(double x, double y) {
        int ielemx = (int) (x / mesh.getDx()) + 1;
        int ielemy = (int) (y / mesh.getDy()) + 1;
        double localx = x - mesh.getDx() * (ielemx - 1);
        double localy = y - mesh.getDy() * (ielemy - 1);
        double solution = 0.0;
        solution += b1.getValue(localx) * b1.getValue(localy) * mRHS[ielemx][ielemy];
        solution += b1.getValue(localx) * b2.getValue(localy) * mRHS[ielemx][ielemy + 1];
        solution += b1.getValue(localx) * b3.getValue(localy) * mRHS[ielemx][ielemy + 2];
        solution += b2.getValue(localx) * b1.getValue(localy) * mRHS[ielemx + 1][ielemy];
        solution += b2.getValue(localx) * b2.getValue(localy) * mRHS[ielemx + 1][ielemy + 1];
        solution += b2.getValue(localx) * b3.getValue(localy) * mRHS[ielemx + 1][ielemy + 2];
        solution += b3.getValue(localx) * b1.getValue(localy) * mRHS[ielemx + 2][ielemy];
        solution += b3.getValue(localx) * b2.getValue(localy) * mRHS[ielemx + 2][ielemy + 1];
        solution += b3.getValue(localx) * b3.getValue(localy) * mRHS[ielemx + 2][ielemy + 2];
        return solution;
    }

    public double getMeanValue() {
        if(meanValue == null) {
            BigDecimal total = BigDecimal.ZERO;
            final int yCount = mRHS.length;
            final int xCount = mRHS[0].length;
            for(int y = 1; y < yCount; y++) {
                for(int x = 1; x < xCount; x++) {
                    total = total.add(new BigDecimal(mRHS[y][x]));
                }
            }
            meanValue = total.divide(new BigDecimal(xCount * yCount), RoundingMode.HALF_EVEN).doubleValue();
            return meanValue;
        } else {
            return meanValue;
        }
    }

    public double[][] getRhs() {
        return mRHS;
    }


    public double getFlood(double x, double y, double floodAvg) {
        int ielemx = (int)(x / (mesh.getDx())) + 1;
        int ielemy = (int)(y / (mesh.getDy())) + 1;
        double localx = x-(mesh.getDx())*(ielemx - 1);
        double localy = y-(mesh.getDy())*(ielemy - 1);
        double solution = 0.0;
        double K = 0.0;

        // Ut
        K += b1.getValue(localx)*mRHS[ielemx][ielemy] - b1.getValue(localx)*mTerrain[ielemx][ielemy];
        K += b1.getValue(localx)*mRHS[ielemx][ielemy+1] - b1.getValue(localx)*mTerrain[ielemx][ielemy+1];
        K += b1.getValue(localx)*mRHS[ielemx][ielemy+2] - b1.getValue(localx)*mTerrain[ielemx][ielemy+2];
        K += b2.getValue(localx)*mRHS[ielemx+1][ielemy] - b2.getValue(localx)*mTerrain[ielemx+1][ielemy];
        K += b2.getValue(localx)*mRHS[ielemx+1][ielemy+1] - b2.getValue(localx)*mTerrain[ielemx+1][ielemy+1];
        K += b2.getValue(localx)*mRHS[ielemx+1][ielemy+2] - b2.getValue(localx)*mTerrain[ielemx+1][ielemy+2];
        K += b3.getValue(localx)*mRHS[ielemx+2][ielemy] - b3.getValue(localx)*mTerrain[ielemx+2][ielemy];
        K += b3.getValue(localx)*mRHS[ielemx+2][ielemy+1] - b3.getValue(localx)*mTerrain[ielemx+2][ielemy+1];
        K += b3.getValue(localx)*mRHS[ielemx+2][ielemy+2] - b3.getValue(localx)*mTerrain[ielemx+2][ielemy+2];

        K = Math.sqrt(K);

        double X = K / floodAvg;

        solution += b1.getFirstDerivativeValueAt(localx)*b1.getFirstDerivativeValueAt(localy)*mRHS[ielemx][ielemy];
        solution += b1.getFirstDerivativeValueAt(localx)*b2.getFirstDerivativeValueAt(localy)*mRHS[ielemx][ielemy+1];
        solution += b1.getFirstDerivativeValueAt(localx)*b3.getFirstDerivativeValueAt(localy)*mRHS[ielemx][ielemy+2];
        solution += b2.getFirstDerivativeValueAt(localx)*b1.getFirstDerivativeValueAt(localy)*mRHS[ielemx+1][ielemy];
        solution += b2.getFirstDerivativeValueAt(localx)*b2.getFirstDerivativeValueAt(localy)*mRHS[ielemx+1][ielemy+1];
        solution += b2.getFirstDerivativeValueAt(localx)*b3.getFirstDerivativeValueAt(localy)*mRHS[ielemx+1][ielemy+2];
        solution += b3.getFirstDerivativeValueAt(localx)*b1.getFirstDerivativeValueAt(localy)*mRHS[ielemx+2][ielemy];
        solution += b3.getFirstDerivativeValueAt(localx)*b2.getFirstDerivativeValueAt(localy)*mRHS[ielemx+2][ielemy+1];
        solution += b3.getFirstDerivativeValueAt(localx)*b3.getFirstDerivativeValueAt(localy)*mRHS[ielemx+2][ielemy+2];

        solution *= X;

        // *

//        solution += b1.getValue(localx)*b1.getSecondDerivativeValueAt(localy)*mRHS[ielemx][ielemy];
//        solution += b1.getValue(localx)*b2.getSecondDerivativeValueAt(localy)*mRHS[ielemx][ielemy+1];
//        solution += b1.getValue(localx)*b3.getSecondDerivativeValueAt(localy)*mRHS[ielemx][ielemy+2];
//        solution += b2.getValue(localx)*b1.getSecondDerivativeValueAt(localy)*mRHS[ielemx+1][ielemy];
//        solution += b2.getValue(localx)*b2.getSecondDerivativeValueAt(localy)*mRHS[ielemx+1][ielemy+1];
//        solution += b2.getValue(localx)*b3.getSecondDerivativeValueAt(localy)*mRHS[ielemx+1][ielemy+2];
//        solution += b3.getValue(localx)*b1.getSecondDerivativeValueAt(localy)*mRHS[ielemx+2][ielemy];
//        solution += b3.getValue(localx)*b2.getSecondDerivativeValueAt(localy)*mRHS[ielemx+2][ielemy+1];
//        solution += b3.getValue(localx)*b3.getSecondDerivativeValueAt(localy)*mRHS[ielemx+2][ielemy+2];

        return solution;
    }

    public double getLaplacian(double x, double y) {
        int ielemx = (int)(x / (mesh.getDx())) + 1;
        int ielemy = (int)(y / (mesh.getDy())) + 1;
        double localx = x-(mesh.getDx())*(ielemx - 1);
        double localy = y-(mesh.getDy())*(ielemy - 1);
        double solution = 0.0;
        solution += b1.getSecondDerivativeValueAt(localx)*b1.getValue(localy)*mRHS[ielemx][ielemy];
        solution += b1.getSecondDerivativeValueAt(localx)*b2.getValue(localy)*mRHS[ielemx][ielemy+1];
        solution += b1.getSecondDerivativeValueAt(localx)*b3.getValue(localy)*mRHS[ielemx][ielemy+2];
        solution += b2.getSecondDerivativeValueAt(localx)*b1.getValue(localy)*mRHS[ielemx+1][ielemy];
        solution += b2.getSecondDerivativeValueAt(localx)*b2.getValue(localy)*mRHS[ielemx+1][ielemy+1];
        solution += b2.getSecondDerivativeValueAt(localx)*b3.getValue(localy)*mRHS[ielemx+1][ielemy+2];
        solution += b3.getSecondDerivativeValueAt(localx)*b1.getValue(localy)*mRHS[ielemx+2][ielemy];
        solution += b3.getSecondDerivativeValueAt(localx)*b2.getValue(localy)*mRHS[ielemx+2][ielemy+1];
        solution += b3.getSecondDerivativeValueAt(localx)*b3.getValue(localy)*mRHS[ielemx+2][ielemy+2];

        solution += b1.getValue(localx)*b1.getSecondDerivativeValueAt(localy)*mRHS[ielemx][ielemy];
        solution += b1.getValue(localx)*b2.getSecondDerivativeValueAt(localy)*mRHS[ielemx][ielemy+1];
        solution += b1.getValue(localx)*b3.getSecondDerivativeValueAt(localy)*mRHS[ielemx][ielemy+2];
        solution += b2.getValue(localx)*b1.getSecondDerivativeValueAt(localy)*mRHS[ielemx+1][ielemy];
        solution += b2.getValue(localx)*b2.getSecondDerivativeValueAt(localy)*mRHS[ielemx+1][ielemy+1];
        solution += b2.getValue(localx)*b3.getSecondDerivativeValueAt(localy)*mRHS[ielemx+1][ielemy+2];
        solution += b3.getValue(localx)*b1.getSecondDerivativeValueAt(localy)*mRHS[ielemx+2][ielemy];
        solution += b3.getValue(localx)*b2.getSecondDerivativeValueAt(localy)*mRHS[ielemx+2][ielemy+1];
        solution += b3.getValue(localx)*b3.getSecondDerivativeValueAt(localy)*mRHS[ielemx+2][ielemy+2];

        return solution;
    }

}
