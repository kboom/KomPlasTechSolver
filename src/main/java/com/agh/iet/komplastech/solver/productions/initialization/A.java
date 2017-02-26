package com.agh.iet.komplastech.solver.productions.initialization;

import com.agh.iet.komplastech.solver.constants.GaussPoints;
import com.agh.iet.komplastech.solver.problem.Problem;
import com.agh.iet.komplastech.solver.productions.Production;
import com.agh.iet.komplastech.solver.splines.BSpline1;
import com.agh.iet.komplastech.solver.splines.BSpline2;
import com.agh.iet.komplastech.solver.splines.BSpline3;
import com.agh.iet.komplastech.solver.splines.Spline;
import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.support.Vertex;

import static com.agh.iet.komplastech.solver.constants.GaussPoints.GAUSS_POINTS;
import static com.agh.iet.komplastech.solver.constants.GaussPoints.GAUSS_POINT_WEIGHTS;
import static com.agh.iet.komplastech.solver.productions.initialization.SampleCoefficients.useArbitraryCoefficients;

public class A extends Production {

    private static final Spline spline1 = new BSpline1();
    private static final Spline spline2 = new BSpline2();
    private static final Spline spline3 = new BSpline3();

    private final Problem problem;

    public A(Mesh mesh, Problem problem) {
        super(mesh);
        this.problem = problem;
    }

    public Vertex apply(Vertex node) {
        initializeCoefficientsMatrix(node);
        initializeRightHandSides(node);
        return node;
    }

    private void initializeCoefficientsMatrix(Vertex node) {
        useArbitraryCoefficients(node);
    }

    private void initializeRightHandSides(Vertex node) {
        for (int i = 1; i <= mesh.getDofsY(); i++) {
            fillRightHandSide(node, spline3, 1, i);
            fillRightHandSide(node, spline2, 2, i);
            fillRightHandSide(node, spline1, 3, i);
        }
    }

    private void fillRightHandSide(Vertex node, Spline spline, int r, int i) {
        for (int k = 1; k <= GaussPoints.GAUSS_POINT_COUNT; k++) {
            double x = GAUSS_POINTS[k] * mesh.getDx() + node.beginning;
            for (int l = 1; l <= GaussPoints.GAUSS_POINT_COUNT; l++) {
                if (i > 2) {
                    double y = (GAUSS_POINTS[l] + (i - 3)) * mesh.getDy();
                    node.m_b[r][i] += GAUSS_POINT_WEIGHTS[k] * spline.getValue(GAUSS_POINTS[k]) * GAUSS_POINT_WEIGHTS[l] * spline1.getValue(GAUSS_POINTS[l]) * problem.getValue(x, y);
                }
                if (i > 1 && (i - 2) < mesh.getElementsY()) {
                    double y = (GAUSS_POINTS[l] + (i - 2)) * mesh.getDy();
                    node.m_b[r][i] += GAUSS_POINT_WEIGHTS[k] * spline.getValue(GAUSS_POINTS[k]) * GAUSS_POINT_WEIGHTS[l] * spline2.getValue(GAUSS_POINTS[l]) * problem.getValue(x, y);
                }
                if ((i - 1) < mesh.getElementsY()) {
                    double y = (GAUSS_POINTS[l] + (i - 1)) * mesh.getDy();
                    node.m_b[r][i] += GAUSS_POINT_WEIGHTS[k] * spline.getValue(GAUSS_POINTS[k]) * GAUSS_POINT_WEIGHTS[l] * spline3.getValue(GAUSS_POINTS[l]) * problem.getValue(x, y);
                }
            }
        }
    }

}
