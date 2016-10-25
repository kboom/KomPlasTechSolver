package com.agh.iet.komplastech.solver;

import java.util.Arrays;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

class Executor extends Thread {
    private static void printMatrix(final Vertex v, int size, int nrhs) {
        for (int i = 1; i <= size; ++i) {
            for (int j = 1; j <= size; ++j) {
                System.out.printf("%6.3f ", v.m_a[i][j]);
            }
            System.out.printf("  |  ");
            for (int j = 1; j <= nrhs; ++j) {
                System.out.printf("%6.3f ", v.m_b[i][j]);
            }
            System.out.printf("  |  ");
            for (int j = 1; j <= nrhs; ++j) {
                System.out.printf("%6.3f ", v.m_x[i][j]);
            }
            System.out.println();
        }
    }

    public synchronized void run() {
        // BUILDING ELEMENT PARTITION TREE
        try {

            int n = 12; // number of elements along x axis
            int m = 12; // number of elements along y axis
            double dx = 12.0; // mesh size along x
            double dy = 12.0; // mesh size along y
            int p = 2; // polynomial order of approximation
            // Mesh
            MeshData mesh = new MeshData(dx, dy, n, m, p);
            Vertex S = new Vertex(null, null, null, null, "Sx", 0, dx, mesh);

            // Build tree along x
            // [(P1)]
            CyclicBarrier barrier = new CyclicBarrier(1 + 1);
            P1 p1 = new P1(S, barrier, mesh);
            p1.start();
            System.out.println("Full: " + p1.m_vertex.m_beg + " - " + p1.m_vertex.m_end);
            barrier.await();
            // [(P2)1(P2)2]
            barrier = new CyclicBarrier(2 + 1);
            System.out.println("p1.m_vertex.m_left" + p1.m_vertex.m_left);
            System.out.println("p1.m_vertex.m_right" + p1.m_vertex.m_right);
            P2 p2a = new P2(p1.m_vertex.m_left, barrier, mesh);
            P2 p2b = new P2(p1.m_vertex.m_right, barrier, mesh);
            p2a.start();
            p2b.start();
            barrier.await();
            // [(P3)1(P3)2(P3)3(P3)4]
            barrier = new CyclicBarrier(4 + 1);
            P3 p3a1 = new P3(p2a.m_vertex.m_left, barrier, mesh);
            P3 p3a2 = new P3(p2a.m_vertex.m_right, barrier, mesh);
            P3 p3b1 = new P3(p2b.m_vertex.m_left, barrier, mesh);
            P3 p3b2 = new P3(p2b.m_vertex.m_right, barrier, mesh);
            p3a1.start();
            p3a2.start();
            p3b1.start();
            p3b2.start();
            barrier.await();

            System.out.println(p3a1.m_vertex.m_beg + " - " + p3a1.m_vertex.m_end);
            System.out.println(p3a2.m_vertex.m_beg + " - " + p3a2.m_vertex.m_end);
            System.out.println(p3b1.m_vertex.m_beg + " - " + p3b1.m_vertex.m_end);
            System.out.println(p3b2.m_vertex.m_beg + " - " + p3b2.m_vertex.m_end);
            // MFS along x
            // [A^12]
            barrier = new CyclicBarrier(12 + 1);
            A1 a1 = new A1(p3a1.m_vertex.m_left, barrier, mesh);
            A a2 = new A(p3a1.m_vertex.m_middle, barrier, mesh);
            A a3 = new A(p3a1.m_vertex.m_right, barrier, mesh);
            A a4 = new A(p3a2.m_vertex.m_left, barrier, mesh);
            A a5 = new A(p3a2.m_vertex.m_middle, barrier, mesh);
            A a6 = new A(p3a2.m_vertex.m_right, barrier, mesh);
            A a7 = new A(p3b1.m_vertex.m_left, barrier, mesh);
            A a8 = new A(p3b1.m_vertex.m_middle, barrier, mesh);
            A a9 = new A(p3b1.m_vertex.m_right, barrier, mesh);
            A a10 = new A(p3b2.m_vertex.m_left, barrier, mesh);
            A a11 = new A(p3b2.m_vertex.m_middle, barrier, mesh);
            AN a12 = new AN(p3b2.m_vertex.m_right, barrier, mesh);
            
            a1.start();
            a2.start();
            a3.start();
            a4.start();
            a5.start();
            a6.start();
            a7.start();
            a8.start();
            a9.start();
            a10.start();
            a11.start();
            a12.start();
            barrier.await();
            
            System.out.println(a1.m_vertex.m_beg + " - " + a1.m_vertex.m_end);
            System.out.println(a2.m_vertex.m_beg + " - " + a2.m_vertex.m_end);
            System.out.println(a3.m_vertex.m_beg + " - " + a3.m_vertex.m_end);
            System.out.println(a4.m_vertex.m_beg + " - " + a4.m_vertex.m_end);
            System.out.println(a5.m_vertex.m_beg + " - " + a5.m_vertex.m_end);
            System.out.println(a6.m_vertex.m_beg + " - " + a6.m_vertex.m_end);
            System.out.println(a7.m_vertex.m_beg + " - " + a7.m_vertex.m_end);
            System.out.println(a8.m_vertex.m_beg + " - " + a8.m_vertex.m_end);
            System.out.println(a9.m_vertex.m_beg + " - " + a9.m_vertex.m_end);
            System.out.println(a10.m_vertex.m_beg + " - " + a10.m_vertex.m_end);
            System.out.println(a11.m_vertex.m_beg + " - " + a11.m_vertex.m_end);
            System.out.println(a12.m_vertex.m_beg + " - " + a12.m_vertex.m_end);
            
            printMatrix(a1.m_vertex, 3, mesh.m_dofsy);
            System.out.println();
            printMatrix(a2.m_vertex, 3, mesh.m_dofsy);
            System.out.println();
            printMatrix(a3.m_vertex, 3, mesh.m_dofsy);
            System.out.println();
            printMatrix(a4.m_vertex, 3, mesh.m_dofsy);
            System.out.println();
            printMatrix(a5.m_vertex, 3, mesh.m_dofsy);
            System.out.println();
            printMatrix(a6.m_vertex, 3, mesh.m_dofsy);
            System.out.println();
            printMatrix(a7.m_vertex, 3, mesh.m_dofsy);
            System.out.println();
            printMatrix(a8.m_vertex, 3, mesh.m_dofsy);
            System.out.println();
            printMatrix(a9.m_vertex, 3, mesh.m_dofsy);
            System.out.println();
            printMatrix(a10.m_vertex, 3, mesh.m_dofsy);
            System.out.println();
            printMatrix(a11.m_vertex, 3, mesh.m_dofsy);
            System.out.println();
            printMatrix(a12.m_vertex, 3, mesh.m_dofsy);
            System.out.println();
            
//            System.exit(0);
            
            // [A2_3^4]
            barrier = new CyclicBarrier(4 + 1);
            A2_3 a2a = new A2_3(p3a1.m_vertex, barrier, mesh);
            A2_3 a2b = new A2_3(p3a2.m_vertex, barrier, mesh);
            A2_3 a2c = new A2_3(p3b1.m_vertex, barrier, mesh);
            A2_3 a2d = new A2_3(p3b2.m_vertex, barrier, mesh);
            a2a.start();
            a2b.start();
            a2c.start();
            a2d.start();
            barrier.await();
            printMatrix(a2a.m_vertex, 5, mesh.m_dofsy);
            System.out.println();
            printMatrix(a2b.m_vertex, 5, mesh.m_dofsy);
            System.out.println();
            printMatrix(a2c.m_vertex, 5, mesh.m_dofsy);
            System.out.println();
            printMatrix(a2d.m_vertex, 5, mesh.m_dofsy);
            System.out.println();
            // [E2_1_5^4]
            barrier = new CyclicBarrier(4 + 1);
            E2_1_5 e2a = new E2_1_5(p3a1.m_vertex, barrier, mesh);
            E2_1_5 e2b = new E2_1_5(p3a2.m_vertex, barrier, mesh);
            E2_1_5 e2c = new E2_1_5(p3b1.m_vertex, barrier, mesh);
            E2_1_5 e2d = new E2_1_5(p3b2.m_vertex, barrier, mesh);
            e2a.start();
            e2b.start();
            e2c.start();
            e2d.start();
            barrier.await();
            printMatrix(a2a.m_vertex, 5, mesh.m_dofsy);
            System.out.println();
            printMatrix(a2b.m_vertex, 5, mesh.m_dofsy);
            System.out.println();
            printMatrix(a2c.m_vertex, 5, mesh.m_dofsy);
            System.out.println();
            printMatrix(a2d.m_vertex, 5, mesh.m_dofsy);
            System.out.println();
            // [A2_2^2]
            barrier = new CyclicBarrier(2 + 1);
            A2_2 a22a = new A2_2(p2a.m_vertex, barrier, mesh);
            A2_2 a22b = new A2_2(p2b.m_vertex, barrier, mesh);
            a22a.start();
            a22b.start();
            barrier.await();
            printMatrix(a22a.m_vertex, 6, mesh.m_dofsy);
            System.out.println();
            printMatrix(a22b.m_vertex, 6, mesh.m_dofsy);
            System.out.println();
            // [E2_2_6^2]
            barrier = new CyclicBarrier(2 + 1);
            E2_2_6 e26a = new E2_2_6(p2a.m_vertex, barrier, mesh);
            E2_2_6 e26b = new E2_2_6(p2b.m_vertex, barrier, mesh);
            e26a.start();
            e26b.start();
            barrier.await();
            printMatrix(a22a.m_vertex, 6, mesh.m_dofsy);
            System.out.println();
            printMatrix(a22b.m_vertex, 6, mesh.m_dofsy);
            System.out.println();
            // [Aroot]
            barrier = new CyclicBarrier(1 + 1);
            Aroot aroot = new Aroot(p1.m_vertex, barrier, mesh);
            aroot.start();
            barrier.await();
            printMatrix(aroot.m_vertex, 6, mesh.m_dofsy);
            // [Eroot]
            barrier = new CyclicBarrier(1 + 1);
            Eroot eroot = new Eroot(p1.m_vertex, barrier, mesh);
            eroot.start();
            barrier.await();
            printMatrix(eroot.m_vertex, 6, mesh.m_dofsy);
            // [BS_1_5^2]
            barrier = new CyclicBarrier(2 + 1);
            BS_2_6 bs1a = new BS_2_6(p2a.m_vertex, barrier, mesh);
            BS_2_6 bs1b = new BS_2_6(p2b.m_vertex, barrier, mesh);
            bs1a.start();
            bs1b.start();
            barrier.await();
            // [BS_2_6^4]
            barrier = new CyclicBarrier(4 + 1);
            BS_1_5 bs2a = new BS_1_5(p3a1.m_vertex, barrier, mesh);
            BS_1_5 bs2b = new BS_1_5(p3a2.m_vertex, barrier, mesh);
            BS_1_5 bs2c = new BS_1_5(p3b1.m_vertex, barrier, mesh);
            BS_1_5 bs2d = new BS_1_5(p3b2.m_vertex, barrier, mesh);
            bs2a.start();
            bs2b.start();
            bs2c.start();
            bs2d.start();
            barrier.await();
            for (BS_1_5 bs : Arrays.asList(bs2a, bs2b, bs2c, bs2d)) {
                printMatrix(bs.m_vertex, 5, mesh.m_dofsy);
                System.out.println();
            }

            double[][] rhs = new double[n * 3 + p + 1][];
            rhs[1] = bs2a.m_vertex.m_x[1];
            rhs[2] = bs2a.m_vertex.m_x[2];
            rhs[3] = bs2a.m_vertex.m_x[3];
            rhs[4] = bs2a.m_vertex.m_x[4];
            rhs[5] = bs2a.m_vertex.m_x[5];
            rhs[6] = bs2b.m_vertex.m_x[3];
            rhs[7] = bs2b.m_vertex.m_x[4];
            rhs[8] = bs2b.m_vertex.m_x[5];
            rhs[9] = bs2c.m_vertex.m_x[3];
            rhs[10] = bs2c.m_vertex.m_x[4];
            rhs[11] = bs2c.m_vertex.m_x[5];
            rhs[12] = bs2d.m_vertex.m_x[3];
            rhs[13] = bs2d.m_vertex.m_x[4];
            rhs[14] = bs2d.m_vertex.m_x[5];

            for (int i = 1; i <= mesh.m_dofsx; ++ i) {
                for (int j = 1; j <= mesh.m_dofsy; ++ j) {
                    System.out.printf("%6.2f ", rhs[i][j]);
                }
                System.out.println();
            }
		
            // REORDER

            // Build tree along y
            S = new Vertex(null,null,null,null,"Sy",0,dy,mesh);
            // [(P1y)]
            barrier = new CyclicBarrier(1 + 1);
            P1y p1y = new P1y(S, barrier, mesh);
            p1y.start();
            barrier.await();
            // [(P2)1(P2)2]
            barrier = new CyclicBarrier(2 + 1);
            System.out.println("p1y.m_vertex.m_left" + p1y.m_vertex.m_left);
            System.out.println("p1y.m_vertex.m_right" + p1y.m_vertex.m_right);
            P2 p2ay = new P2(p1y.m_vertex.m_left, barrier, mesh);
            P2 p2by = new P2(p1y.m_vertex.m_right, barrier, mesh);
            p2ay.start();
            p2by.start();
            barrier.await();
            // [(P3)1(P3)2(P3)3(P3)4]
            barrier = new CyclicBarrier(4 + 1);
            P3 p3a1y = new P3(p2ay.m_vertex.m_left, barrier, mesh);
            P3 p3a2y = new P3(p2ay.m_vertex.m_right, barrier, mesh);
            P3 p3b1y = new P3(p2by.m_vertex.m_left, barrier, mesh);
            P3 p3b2y = new P3(p2by.m_vertex.m_right, barrier, mesh);
            p3a1y.start();
            p3a2y.start();
            p3b1y.start();
            p3b2y.start();
            barrier.await();

            barrier = new CyclicBarrier(12 + 1);
            A1y a1y = new A1y(p3a1y.m_vertex.m_left, rhs, new double[]{1, 1./2, 1./3}, 0, barrier, mesh);
            Ay a2y = new Ay(p3a1y.m_vertex.m_middle, rhs, new double[]{1./2, 1./3, 1./3}, 1, barrier, mesh);
            Ay a3y = new Ay(p3a1y.m_vertex.m_right, rhs, new double[]{1./3, 1./3, 1./3}, 2, barrier, mesh);
            Ay a4y = new Ay(p3a2y.m_vertex.m_left, rhs, new double[]{1./3, 1./3, 1./3}, 3, barrier, mesh);
            Ay a5y = new Ay(p3a2y.m_vertex.m_middle, rhs, new double[]{1./3, 1./3, 1./3}, 4, barrier, mesh);
            Ay a6y = new Ay(p3a2y.m_vertex.m_right, rhs, new double[]{1./3, 1./3, 1./3}, 5, barrier, mesh);
            Ay a7y = new Ay(p3b1y.m_vertex.m_left, rhs, new double[]{1./3, 1./3, 1./3}, 6, barrier, mesh);
            Ay a8y = new Ay(p3b1y.m_vertex.m_middle, rhs, new double[]{1./3, 1./3, 1./3}, 7, barrier, mesh);
            Ay a9y = new Ay(p3b1y.m_vertex.m_right, rhs, new double[]{1./3, 1./3, 1./3}, 8, barrier, mesh);
            Ay a10y = new Ay(p3b2y.m_vertex.m_left, rhs, new double[]{1./3, 1./3, 1./3}, 9, barrier, mesh);
            Ay a11y = new Ay(p3b2y.m_vertex.m_middle, rhs, new double[]{1./3, 1./3, 1./2}, 10, barrier, mesh);
            ANy a12y = new ANy(p3b2y.m_vertex.m_right, rhs, new double[]{1./3, 1./2, 1}, 11, barrier, mesh);
            
            a1y.start();
            a2y.start();
            a3y.start();
            a4y.start();
            a5y.start();
            a6y.start();
            a7y.start();
            a8y.start();
            a9y.start();
            a10y.start();
            a11y.start();
            a12y.start();
            barrier.await();
            printMatrix(a1y.m_vertex, 3, mesh.m_dofsx);
            System.out.println();
            printMatrix(a2y.m_vertex, 3, mesh.m_dofsx);
            System.out.println();
            printMatrix(a3y.m_vertex, 3, mesh.m_dofsx);
            System.out.println();
            printMatrix(a4y.m_vertex, 3, mesh.m_dofsx);
            System.out.println();
            printMatrix(a5y.m_vertex, 3, mesh.m_dofsx);
            System.out.println();
            printMatrix(a6y.m_vertex, 3, mesh.m_dofsx);
            System.out.println();
            printMatrix(a7y.m_vertex, 3, mesh.m_dofsx);
            System.out.println();
            printMatrix(a8y.m_vertex, 3, mesh.m_dofsx);
            System.out.println();
            printMatrix(a9y.m_vertex, 3, mesh.m_dofsx);
            System.out.println();
            printMatrix(a10y.m_vertex, 3, mesh.m_dofsx);
            System.out.println();
            printMatrix(a11y.m_vertex, 3, mesh.m_dofsx);
            System.out.println();
            printMatrix(a12y.m_vertex, 3, mesh.m_dofsx);
            System.out.println();
            // [A2_3^4]
            barrier = new CyclicBarrier(4 + 1);
            a2a = new A2_3(p3a1y.m_vertex, barrier, mesh);
            a2b = new A2_3(p3a2y.m_vertex, barrier, mesh);
            a2c = new A2_3(p3b1y.m_vertex, barrier, mesh);
            a2d = new A2_3(p3b2y.m_vertex, barrier, mesh);
            a2a.start();
            a2b.start();
            a2c.start();
            a2d.start();
            barrier.await();
            printMatrix(a2a.m_vertex, 5, mesh.m_dofsx);
            System.out.println();
            printMatrix(a2b.m_vertex, 5, mesh.m_dofsx);
            System.out.println();
            printMatrix(a2c.m_vertex, 5, mesh.m_dofsx);
            System.out.println();
            printMatrix(a2d.m_vertex, 5, mesh.m_dofsx);
            System.out.println();
            // [E2_1_5^4]
            barrier = new CyclicBarrier(4 + 1);
            e2a = new E2_1_5(p3a1y.m_vertex, barrier, mesh);
            e2b = new E2_1_5(p3a2y.m_vertex, barrier, mesh);
            e2c = new E2_1_5(p3b1y.m_vertex, barrier, mesh);
            e2d = new E2_1_5(p3b2y.m_vertex, barrier, mesh);
            e2a.start();
            e2b.start();
            e2c.start();
            e2d.start();
            barrier.await();
            printMatrix(a2a.m_vertex, 5, mesh.m_dofsx);
            System.out.println();
            printMatrix(a2b.m_vertex, 5, mesh.m_dofsx);
            System.out.println();
            printMatrix(a2c.m_vertex, 5, mesh.m_dofsx);
            System.out.println();
            printMatrix(a2d.m_vertex, 5, mesh.m_dofsx);
            System.out.println();
            // [A2_2^2]
            barrier = new CyclicBarrier(2 + 1);
            a22a = new A2_2(p2ay.m_vertex, barrier, mesh);
            a22b = new A2_2(p2by.m_vertex, barrier, mesh);
            a22a.start();
            a22b.start();
            barrier.await();
            printMatrix(a22a.m_vertex, 6, mesh.m_dofsy);
            System.out.println();
            printMatrix(a22b.m_vertex, 6, mesh.m_dofsy);
            System.out.println();
            // [E2_2_6^2]
            barrier = new CyclicBarrier(2 + 1);
            e26a = new E2_2_6(p2ay.m_vertex, barrier, mesh);
            e26b = new E2_2_6(p2by.m_vertex, barrier, mesh);
            e26a.start();
            e26b.start();
            barrier.await();
            printMatrix(a22a.m_vertex, 6, mesh.m_dofsx);
            System.out.println();
            printMatrix(a22b.m_vertex, 6, mesh.m_dofsx);
            System.out.println();
            // [Aroot]
            barrier = new CyclicBarrier(1 + 1);
            aroot = new Aroot(p1y.m_vertex, barrier, mesh);
            aroot.start();
            barrier.await();
            printMatrix(aroot.m_vertex, 6, mesh.m_dofsx);
            // [Eroot]
            barrier = new CyclicBarrier(1 + 1);
            eroot = new Eroot(p1y.m_vertex, barrier, mesh);
            eroot.start();
            barrier.await();
            printMatrix(aroot.m_vertex, 6, mesh.m_dofsx);
            // [BS_1_5^2]
            barrier = new CyclicBarrier(2 + 1);
            bs1a = new BS_2_6(p2ay.m_vertex, barrier, mesh);
            bs1b = new BS_2_6(p2by.m_vertex, barrier, mesh);
            bs1a.start();
            bs1b.start();
            barrier.await();
            // [BS_2_6^4]
            barrier = new CyclicBarrier(4 + 1);
            bs2a = new BS_1_5(p3a1y.m_vertex, barrier, mesh);
            bs2b = new BS_1_5(p3a2y.m_vertex, barrier, mesh);
            bs2c = new BS_1_5(p3b1y.m_vertex, barrier, mesh);
            bs2d = new BS_1_5(p3b2y.m_vertex, barrier, mesh);
            bs2a.start();
            bs2b.start();
            bs2c.start();
            bs2d.start();
            barrier.await();
            
            rhs[1] = bs2a.m_vertex.m_x[1];
            rhs[2] = bs2a.m_vertex.m_x[2];
            rhs[3] = bs2a.m_vertex.m_x[3];
            rhs[4] = bs2a.m_vertex.m_x[4];
            rhs[5] = bs2a.m_vertex.m_x[5];
            rhs[6] = bs2b.m_vertex.m_x[3];
            rhs[7] = bs2b.m_vertex.m_x[4];
            rhs[8] = bs2b.m_vertex.m_x[5];
            rhs[9] = bs2c.m_vertex.m_x[3];
            rhs[10] = bs2c.m_vertex.m_x[4];
            rhs[11] = bs2c.m_vertex.m_x[5];
            rhs[12] = bs2d.m_vertex.m_x[3];
            rhs[13] = bs2d.m_vertex.m_x[4];
            rhs[14] = bs2d.m_vertex.m_x[5];
            
            for (int i = 1; i <= mesh.m_dofsx; ++ i) {
                for (int j = 1; j <= mesh.m_dofsy; ++ j) {
                    System.out.printf("%6.2f ", rhs[i][j]);
                }
                System.out.println();
            }

			System.out.println("Solution");
						
			Solution solEval = new Solution(mesh,rhs);
			
			double Dx = (mesh.m_dx/mesh.m_nelemx);			
			double Dy = (mesh.m_dy/mesh.m_nelemy);
			double x=-Dx/2;
			double y=-Dy/2;
            for (int i = 1; i <= mesh.m_nelemx; ++ i) {
            	x += Dx;
            	y = -Dy/2;
                for (int j = 1; j <= mesh.m_nelemy; ++ j) {
                	y += Dy;
                	double solution = solEval.get_value(x,y);
                	System.out.printf("(%5.2f, %5.2f): %6.2f ", x, y, solution);
//                    System.out.printf("%6.2f ", solution);
                }
                System.out.println();
            }
            
            System.exit(0);
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}