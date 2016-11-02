package com.agh.iet.komplastech.solver.productions;

import com.agh.iet.komplastech.solver.GaussPoints;
import com.agh.iet.komplastech.solver.Mesh;
import com.agh.iet.komplastech.solver.RightHandSide;
import com.agh.iet.komplastech.solver.Vertex;
import com.agh.iet.komplastech.solver.splines.Bspline1;
import com.agh.iet.komplastech.solver.splines.Bspline2;
import com.agh.iet.komplastech.solver.splines.Bspline3;

import static com.agh.iet.komplastech.solver.GaussPoints.GAUSS_POINTS;
import static com.agh.iet.komplastech.solver.GaussPoints.GAUSS_POINT_WEIGHTS;

public class Agen extends Production {
    Agen(Vertex Vert, Mesh Mesh) {
        super(Vert, Mesh);
    }

    Vertex apply(Vertex T) {
        System.out.println("Agen");
        Bspline1 b1 = new Bspline1();
        Bspline2 b2 = new Bspline2();
        Bspline3 b3 = new Bspline3();
        RightHandSide rhs = new RightHandSide();
        // a[1][1] integral B1*B1
        T.m_a[1][1] = 0.0;
        for (int i = 1; i <= GaussPoints.GAUSS_POINT_COUNT; i++) {
            T.m_a[1][1] += GAUSS_POINT_WEIGHTS[i] * b1.get_value(GAUSS_POINTS[i]) * b1.get_value(GAUSS_POINTS[i]);
            // System.out.println("m_gauss.GAUSS_POINT_WEIGHTS[i]:"+m_gauss.GAUSS_POINT_WEIGHTS[i]);
            // System.out.println("m_gauss.GAUSS_POINTS[i]:"+m_gauss.GAUSS_POINTS[i]);
            // System.out.println("b1.get_value("+m_gauss.GAUSS_POINTS[i]+")="+b1.get_value(m_gauss.GAUSS_POINTS[i]));
        }
        // a[1][2] integral B1*B2
        T.m_a[1][2] = 0.0;
        for (int i = 1; i <= GaussPoints.GAUSS_POINT_COUNT; i++)
            T.m_a[1][2] += GAUSS_POINT_WEIGHTS[i] * b1.get_value(GAUSS_POINTS[i]) * b2.get_value(GAUSS_POINTS[i]);
        // a[1][3] integral B1*B3
        T.m_a[1][3] = 0.0;
        for (int i = 1; i <= GaussPoints.GAUSS_POINT_COUNT; i++)
            T.m_a[1][3] += GAUSS_POINT_WEIGHTS[i] * b1.get_value(GAUSS_POINTS[i]) * b3.get_value(GAUSS_POINTS[i]);
        // a[2][1] integral B2*B1
        T.m_a[2][1] = T.m_a[1][2];
        // a[2][2] integral B2*B2
        T.m_a[2][2] = 0.0;
        for (int i = 1; i <= GaussPoints.GAUSS_POINT_COUNT; i++)
            T.m_a[2][2] += GAUSS_POINT_WEIGHTS[i] * b2.get_value(GAUSS_POINTS[i]) * b2.get_value(GAUSS_POINTS[i]);
        // a[2][3] integral B2*B3
        T.m_a[2][3] = 0.0;
        for (int i = 1; i <= GaussPoints.GAUSS_POINT_COUNT; i++)
            T.m_a[2][3] += GAUSS_POINT_WEIGHTS[i] * b2.get_value(GAUSS_POINTS[i]) * b3.get_value(GAUSS_POINTS[i]);
        // a[3][1] integral B3*B1
        T.m_a[3][1] = T.m_a[1][3];
        // a[3][2] integral B3*B2
        T.m_a[3][2] = T.m_a[2][3];
        // a[3][3] integral B3*B3
        T.m_a[3][3] = 0.0;
        for (int i = 1; i <= GaussPoints.GAUSS_POINT_COUNT; i++)
            T.m_a[3][3] += GAUSS_POINT_WEIGHTS[i] * b3.get_value(GAUSS_POINTS[i]) * b3.get_value(GAUSS_POINTS[i]);

        // multiple right-hand sides
        for (int j = 1; j <= T.m_mesh.getElementsY(); j++) {
            // b[1][j] integral B1*f over row ******************** along element
            // ex1 (length = mesh size along y)
            T.m_b[1][j] = 0.0;
            for (int i = 1; i <= T.m_mesh.getElementsX(); i++) {
                for (int k = 1; k <= GaussPoints.GAUSS_POINT_COUNT; k++) {
                    for (int l = 1; l <= GaussPoints.GAUSS_POINT_COUNT; l++) {
                        T.m_b[1][j] += GAUSS_POINT_WEIGHTS[k] * GAUSS_POINT_WEIGHTS[l] * b1.get_value(GAUSS_POINTS[k])
                                * rhs.get_value((i - 1) + GAUSS_POINTS[k], (j - 1) + GAUSS_POINTS[l]);
                    }
                }
            }
            // b[2][j] integral B2*f
            T.m_b[2][j] = 0.0;
            for (int i = 1; i <= T.m_mesh.getElementsX(); i++) {
                for (int k = 1; k <= GaussPoints.GAUSS_POINT_COUNT; k++) {
                    for (int l = 1; l <= GaussPoints.GAUSS_POINT_COUNT; l++) {
                        T.m_b[2][j] += GAUSS_POINT_WEIGHTS[k] * GAUSS_POINT_WEIGHTS[l] * b2.get_value(GAUSS_POINTS[k])
                                * rhs.get_value((i - 1) + GAUSS_POINTS[k], (j - 1) + GAUSS_POINTS[l]);
                    }
                }
            }
            // b[3][j] integral B3*f
            T.m_b[3][j] = 0.0;
            for (int i = 1; i <= T.m_mesh.getElementsX(); i++) {
                for (int k = 1; k <= GaussPoints.GAUSS_POINT_COUNT; k++) {
                    for (int l = 1; l <= GaussPoints.GAUSS_POINT_COUNT; l++) {
                        T.m_b[3][j] += GAUSS_POINT_WEIGHTS[k] * GAUSS_POINT_WEIGHTS[l] * b3.get_value(GAUSS_POINTS[k])
                                * rhs.get_value((i - 1) + GAUSS_POINTS[k], (j - 1) + GAUSS_POINTS[l]);
                    }
                }
            }
        }
        return T;
    }
}
