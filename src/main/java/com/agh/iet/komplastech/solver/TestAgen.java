package com.agh.iet.komplastech.solver;

import com.agh.iet.komplastech.solver.productions.A;
import com.agh.iet.komplastech.solver.productions.Agen;

import java.util.concurrent.CyclicBarrier;

public class TestAgen {
    TestAgen() {
    }

    boolean Test(A a, Agen agen, CyclicBarrier Barrier, MeshData Mesh) {

        // System.out.println(agen.m_gauss.m_points[1]+","+agen.m_gauss.m_points[2]+","+agen.m_gauss.m_points[3]+","+agen.m_gauss.m_points[4]+","+agen.m_gauss.m_points[5]);
        // System.out.println(agen.m_gauss.m_weights[1]+","+agen.m_gauss.m_weights[2]+","+agen.m_gauss.m_weights[3]+","+agen.m_gauss.m_weights[4]+","+agen.m_gauss.m_weights[5]);

        System.out.println("A matrix:");
        System.out.println(a.m_vertex.m_a[1][1] + "," + a.m_vertex.m_a[1][2] + "," + a.m_vertex.m_a[1][3]);
        System.out.println(a.m_vertex.m_a[2][1] + "," + a.m_vertex.m_a[2][2] + "," + a.m_vertex.m_a[2][3]);
        System.out.println(a.m_vertex.m_a[3][1] + "," + a.m_vertex.m_a[3][2] + "," + a.m_vertex.m_a[3][3]);
        System.out.println("Agen matrix:");
        System.out.println(agen.m_vertex.m_a[1][1] + "," + agen.m_vertex.m_a[1][2] + "," + agen.m_vertex.m_a[1][3]);
        System.out.println(agen.m_vertex.m_a[2][1] + "," + agen.m_vertex.m_a[2][2] + "," + agen.m_vertex.m_a[2][3]);
        System.out.println(agen.m_vertex.m_a[3][1] + "," + agen.m_vertex.m_a[3][2] + "," + agen.m_vertex.m_a[3][3]);
        System.out.println("A rhs:");
        System.out.println(a.m_vertex.m_b[1][1] + "," + a.m_vertex.m_b[1][2] + "," + a.m_vertex.m_b[1][3]);
        System.out.println("Agen rhs:");
        System.out.println(agen.m_vertex.m_b[1][1] + "," + agen.m_vertex.m_b[1][2] + "," + agen.m_vertex.m_b[1][3]);

        double diff_mat = Math.sqrt(Math.pow(a.m_vertex.m_a[1][1] - agen.m_vertex.m_a[1][1], 2)
                + Math.pow(a.m_vertex.m_a[2][1] - agen.m_vertex.m_a[2][1], 2)
                + Math.pow(a.m_vertex.m_a[3][1] - agen.m_vertex.m_a[3][1], 2)
                + Math.pow(a.m_vertex.m_a[1][2] - agen.m_vertex.m_a[1][2], 2)
                + Math.pow(a.m_vertex.m_a[2][2] - agen.m_vertex.m_a[2][2], 2)
                + Math.pow(a.m_vertex.m_a[3][2] - agen.m_vertex.m_a[3][2], 2)
                + Math.pow(a.m_vertex.m_a[1][3] - agen.m_vertex.m_a[1][3], 2)
                + Math.pow(a.m_vertex.m_a[2][3] - agen.m_vertex.m_a[2][3], 2)
                + Math.pow(a.m_vertex.m_a[3][3] - agen.m_vertex.m_a[3][3], 2));
        /*
         * diff_mat = Math.sqrt( Math.pow( a.m_vertex.m_a[1][1], 2) + Math.pow(
         * a.m_vertex.m_a[2][1], 2) + Math.pow( a.m_vertex.m_a[3][1], 2) +
         * Math.pow( a.m_vertex.m_a[1][2], 2) + Math.pow( a.m_vertex.m_a[2][2],
         * 2) + Math.pow( a.m_vertex.m_a[3][2], 2) + Math.pow(
         * a.m_vertex.m_a[1][3], 2) + Math.pow( a.m_vertex.m_a[2][3], 2) +
         * Math.pow( a.m_vertex.m_a[3][3], 2) );
         */
        System.out.println("diff_mat:" + diff_mat);
        double diff_vect = 0.0;
        for (int i = 1; i < Mesh.m_nelemy; i++)
            diff_vect += Math.pow(a.m_vertex.m_b[1][i] - agen.m_vertex.m_b[1][i], 2)
                    + Math.pow(a.m_vertex.m_b[2][i] - agen.m_vertex.m_b[2][i], 2)
                    + Math.pow(a.m_vertex.m_b[3][i] - agen.m_vertex.m_b[3][i], 2);
        diff_vect = Math.sqrt(diff_vect);
        System.out.println("diff_vect:" + diff_vect);
        if (Math.abs(diff_mat) < 0.00000001 && Math.abs(diff_vect) < 0.00000001)
            return true;
        else
            return false;
    }
}
