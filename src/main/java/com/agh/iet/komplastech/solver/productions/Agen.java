package com.agh.iet.komplastech.solver.productions;

import com.agh.iet.komplastech.solver.GaussPoints;
import com.agh.iet.komplastech.solver.MeshData;
import com.agh.iet.komplastech.solver.RightHandSide;
import com.agh.iet.komplastech.solver.Vertex;
import com.agh.iet.komplastech.solver.splines.Bspline1;
import com.agh.iet.komplastech.solver.splines.Bspline2;
import com.agh.iet.komplastech.solver.splines.Bspline3;

import java.util.concurrent.CyclicBarrier;

public class Agen extends Production {
    Agen(Vertex Vert, MeshData Mesh) {
        super(Vert, Mesh);
        m_gauss = new GaussPoints();
    }

    GaussPoints m_gauss;

    Vertex apply(Vertex T) {
        System.out.println("Agen");
        Bspline1 b1 = new Bspline1();
        Bspline2 b2 = new Bspline2();
        Bspline3 b3 = new Bspline3();
        RightHandSide rhs = new RightHandSide();
        // a[1][1] integral B1*B1
        T.m_a[1][1] = 0.0;
        for (int i = 1; i <= m_gauss.m_nr_points; i++) {
            T.m_a[1][1] += m_gauss.m_weights[i] * b1.get_value(m_gauss.m_points[i]) * b1.get_value(m_gauss.m_points[i]);
            // System.out.println("m_gauss.m_weights[i]:"+m_gauss.m_weights[i]);
            // System.out.println("m_gauss.m_points[i]:"+m_gauss.m_points[i]);
            // System.out.println("b1.get_value("+m_gauss.m_points[i]+")="+b1.get_value(m_gauss.m_points[i]));
        }
        // a[1][2] integral B1*B2
        T.m_a[1][2] = 0.0;
        for (int i = 1; i <= m_gauss.m_nr_points; i++)
            T.m_a[1][2] += m_gauss.m_weights[i] * b1.get_value(m_gauss.m_points[i]) * b2.get_value(m_gauss.m_points[i]);
        // a[1][3] integral B1*B3
        T.m_a[1][3] = 0.0;
        for (int i = 1; i <= m_gauss.m_nr_points; i++)
            T.m_a[1][3] += m_gauss.m_weights[i] * b1.get_value(m_gauss.m_points[i]) * b3.get_value(m_gauss.m_points[i]);
        // a[2][1] integral B2*B1
        T.m_a[2][1] = T.m_a[1][2];
        // a[2][2] integral B2*B2
        T.m_a[2][2] = 0.0;
        for (int i = 1; i <= m_gauss.m_nr_points; i++)
            T.m_a[2][2] += m_gauss.m_weights[i] * b2.get_value(m_gauss.m_points[i]) * b2.get_value(m_gauss.m_points[i]);
        // a[2][3] integral B2*B3
        T.m_a[2][3] = 0.0;
        for (int i = 1; i <= m_gauss.m_nr_points; i++)
            T.m_a[2][3] += m_gauss.m_weights[i] * b2.get_value(m_gauss.m_points[i]) * b3.get_value(m_gauss.m_points[i]);
        // a[3][1] integral B3*B1
        T.m_a[3][1] = T.m_a[1][3];
        // a[3][2] integral B3*B2
        T.m_a[3][2] = T.m_a[2][3];
        // a[3][3] integral B3*B3
        T.m_a[3][3] = 0.0;
        for (int i = 1; i <= m_gauss.m_nr_points; i++)
            T.m_a[3][3] += m_gauss.m_weights[i] * b3.get_value(m_gauss.m_points[i]) * b3.get_value(m_gauss.m_points[i]);

        // multiple right-hand sides
        for (int j = 1; j <= T.m_mesh.m_nelemy; j++) {
            // b[1][j] integral B1*f over row ******************** along element
            // ex1 (length = mesh size along y)
            T.m_b[1][j] = 0.0;
            for (int i = 1; i <= T.m_mesh.m_nelemx; i++) {
                for (int k = 1; k <= m_gauss.m_nr_points; k++) {
                    for (int l = 1; l <= m_gauss.m_nr_points; l++) {
                        T.m_b[1][j] += m_gauss.m_weights[k] * m_gauss.m_weights[l] * b1.get_value(m_gauss.m_points[k])
                                * rhs.get_value((i - 1) + m_gauss.m_points[k], (j - 1) + m_gauss.m_points[l]);
                    }
                }
            }
            // b[2][j] integral B2*f
            T.m_b[2][j] = 0.0;
            for (int i = 1; i <= T.m_mesh.m_nelemx; i++) {
                for (int k = 1; k <= m_gauss.m_nr_points; k++) {
                    for (int l = 1; l <= m_gauss.m_nr_points; l++) {
                        T.m_b[2][j] += m_gauss.m_weights[k] * m_gauss.m_weights[l] * b2.get_value(m_gauss.m_points[k])
                                * rhs.get_value((i - 1) + m_gauss.m_points[k], (j - 1) + m_gauss.m_points[l]);
                    }
                }
            }
            // b[3][j] integral B3*f
            T.m_b[3][j] = 0.0;
            for (int i = 1; i <= T.m_mesh.m_nelemx; i++) {
                for (int k = 1; k <= m_gauss.m_nr_points; k++) {
                    for (int l = 1; l <= m_gauss.m_nr_points; l++) {
                        T.m_b[3][j] += m_gauss.m_weights[k] * m_gauss.m_weights[l] * b3.get_value(m_gauss.m_points[k])
                                * rhs.get_value((i - 1) + m_gauss.m_points[k], (j - 1) + m_gauss.m_points[l]);
                    }
                }
            }
        }
        return T;
    }
}
