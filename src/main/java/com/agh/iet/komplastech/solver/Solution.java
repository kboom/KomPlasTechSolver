package com.agh.iet.komplastech.solver;

import com.agh.iet.komplastech.solver.splines.BSpline1;
import com.agh.iet.komplastech.solver.splines.BSpline2;
import com.agh.iet.komplastech.solver.splines.BSpline3;
import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.support.Point;

import static com.agh.iet.komplastech.solver.SolutionGrid.solutionGrid;
import static com.agh.iet.komplastech.solver.support.Point.solutionPoint;

public abstract class Solution {

    protected static final BSpline1 b1 = new BSpline1();
    protected static final BSpline2 b2 = new BSpline2();
    protected static final BSpline3 b3 = new BSpline3();

    protected final Mesh mesh;
    protected final double[][] mRHS;

    public Solution(Mesh mesh, double[][] rhs) {
        this.mesh = mesh;
        mRHS = rhs;
    }

    public final double[][] getRhs() {
        return mRHS;
    }

    final SolutionGrid getSolutionGrid() {
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

    public final double getValue(double x, double y) {
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

    public abstract double getModifiedValue(double x, double y);

    public Mesh getMesh() {
        return mesh;
    }
}
