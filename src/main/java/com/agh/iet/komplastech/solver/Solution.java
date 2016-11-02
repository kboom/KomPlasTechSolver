package com.agh.iet.komplastech.solver;

import com.agh.iet.komplastech.solver.splines.BSpline1;
import com.agh.iet.komplastech.solver.splines.BSpline2;
import com.agh.iet.komplastech.solver.splines.BSpline3;

import static com.agh.iet.komplastech.solver.Point.solutionPoint;
import static com.agh.iet.komplastech.solver.SolutionGrid.solutionGrid;

class Solution {

    private Mesh m_mesh;
    private double[][] m_rhs;

    Solution(Mesh mesh, double[][] rhs) {
        m_mesh = mesh;
        m_rhs = rhs;
    }

    SolutionGrid getSolutionGrid() {
        double Dx = (m_mesh.getResolutionX() / m_mesh.getElementsX());
        double Dy = (m_mesh.getResolutionY() / m_mesh.getElementsY());
        double x = -Dx / 2;
        double y = -Dy / 2;
        SolutionGrid solutionGrid = solutionGrid();
        for (int i = 1; i <= m_mesh.getElementsX(); ++i) {
            x += Dx;
            y = -Dy / 2;
            for (int j = 1; j <= m_mesh.getElementsY(); ++j) {
                y += Dy;
                Point point = solutionPoint(x, y, getValue(x, y));
                solutionGrid.addPoint(point);
            }
        }
        return solutionGrid;
    }

    private double getValue(double x, double y) {
        int ielemx = (int) (x / (m_mesh.getResolutionX() / m_mesh.getElementsX())) + 1;
        int ielemy = (int) (y / (m_mesh.getResolutionY() / m_mesh.getElementsY())) + 1;
        double localx = x - (m_mesh.getResolutionX() / m_mesh.getElementsX()) * (ielemx - 1);
        double localy = y - (m_mesh.getResolutionY() / m_mesh.getElementsY()) * (ielemy - 1);
        BSpline1 b1 = new BSpline1();
        BSpline2 b2 = new BSpline2();
        BSpline3 b3 = new BSpline3();
        double solution = 0.0;
        solution += b1.getValue(localx) * b1.getValue(localy) * m_rhs[ielemx][ielemy];
        solution += b1.getValue(localx) * b2.getValue(localy) * m_rhs[ielemx][ielemy + 1];
        solution += b1.getValue(localx) * b3.getValue(localy) * m_rhs[ielemx][ielemy + 2];
        solution += b2.getValue(localx) * b1.getValue(localy) * m_rhs[ielemx + 1][ielemy];
        solution += b2.getValue(localx) * b2.getValue(localy) * m_rhs[ielemx + 1][ielemy + 1];
        solution += b2.getValue(localx) * b3.getValue(localy) * m_rhs[ielemx + 1][ielemy + 2];
        solution += b3.getValue(localx) * b1.getValue(localy) * m_rhs[ielemx + 2][ielemy];
        solution += b3.getValue(localx) * b2.getValue(localy) * m_rhs[ielemx + 2][ielemy + 1];
        solution += b3.getValue(localx) * b3.getValue(localy) * m_rhs[ielemx + 2][ielemy + 2];
        return solution;
    }
}
