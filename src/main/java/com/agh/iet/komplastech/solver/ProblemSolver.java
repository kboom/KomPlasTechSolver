package com.agh.iet.komplastech.solver;

import com.agh.iet.komplastech.solver.productions.*;
import org.apache.log4j.Logger;

import java.util.Arrays;

class ProblemSolver {

    private Logger logger = Logger.getLogger(ProblemSolver.class);

    private final Mesh mesh;

    private ProductionLauncherFactory launcherFactory = new ProductionLauncherFactory();

    ProblemSolver(Mesh meshData) {
        this.mesh = meshData;
    }

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
        }
    }

    Solution solveProblem() throws Exception {
        // BUILDING ELEMENT PARTITION TREE


        Vertex S = new Vertex(null, null, null, null, "Sx", 0, mesh.getResolutionX(), mesh);

        // Build tree along x
        // [(P1)]
        P1 p1 = new P1(S, mesh);

        launcherFactory
                .createLauncherFor(p1)
                .launchProductions();

        logger.debug("Full: " + p1.m_vertex.m_beg + " - " + p1.m_vertex.m_end);

        logger.debug("p1.m_vertex.m_left" + p1.m_vertex.m_left);
        logger.debug("p1.m_vertex.m_right" + p1.m_vertex.m_right);
        P2 p2a = new P2(p1.m_vertex.m_left, mesh);
        P2 p2b = new P2(p1.m_vertex.m_right, mesh);

        launcherFactory
                .createLauncherFor(p2a, p2b)
                .launchProductions();

        P3 p3a1 = new P3(p2a.m_vertex.m_left, mesh);
        P3 p3a2 = new P3(p2a.m_vertex.m_right, mesh);
        P3 p3b1 = new P3(p2b.m_vertex.m_left, mesh);
        P3 p3b2 = new P3(p2b.m_vertex.m_right, mesh);

        launcherFactory
                .createLauncherFor(p3a1, p3a2, p3b1, p3b2)
                .launchProductions();

        logger.debug(p3a1.m_vertex.m_beg + " - " + p3a1.m_vertex.m_end);
        logger.debug(p3a2.m_vertex.m_beg + " - " + p3a2.m_vertex.m_end);
        logger.debug(p3b1.m_vertex.m_beg + " - " + p3b1.m_vertex.m_end);
        logger.debug(p3b2.m_vertex.m_beg + " - " + p3b2.m_vertex.m_end);


        A1 a1 = new A1(p3a1.m_vertex.m_left, mesh);
        A a2 = new A(p3a1.m_vertex.m_middle, mesh);
        A a3 = new A(p3a1.m_vertex.m_right, mesh);
        A a4 = new A(p3a2.m_vertex.m_left, mesh);
        A a5 = new A(p3a2.m_vertex.m_middle, mesh);
        A a6 = new A(p3a2.m_vertex.m_right, mesh);
        A a7 = new A(p3b1.m_vertex.m_left, mesh);
        A a8 = new A(p3b1.m_vertex.m_middle, mesh);
        A a9 = new A(p3b1.m_vertex.m_right, mesh);
        A a10 = new A(p3b2.m_vertex.m_left, mesh);
        A a11 = new A(p3b2.m_vertex.m_middle, mesh);
        AN a12 = new AN(p3b2.m_vertex.m_right, mesh);

        launcherFactory
                .createLauncherFor(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12)
                .launchProductions();

        A2_3 a2a = new A2_3(p3a1.m_vertex, mesh);
        A2_3 a2b = new A2_3(p3a2.m_vertex, mesh);
        A2_3 a2c = new A2_3(p3b1.m_vertex, mesh);
        A2_3 a2d = new A2_3(p3b2.m_vertex, mesh);

        launcherFactory
                .createLauncherFor(a2a, a2b, a2c, a2d)
                .launchProductions();


        printMatrix(a2a.m_vertex, 5, mesh.getDofsY());

        printMatrix(a2b.m_vertex, 5, mesh.getDofsY());

        printMatrix(a2c.m_vertex, 5, mesh.getDofsY());

        printMatrix(a2d.m_vertex, 5, mesh.getDofsY());

        E2_1_5 e2a = new E2_1_5(p3a1.m_vertex, mesh);
        E2_1_5 e2b = new E2_1_5(p3a2.m_vertex, mesh);
        E2_1_5 e2c = new E2_1_5(p3b1.m_vertex, mesh);
        E2_1_5 e2d = new E2_1_5(p3b2.m_vertex, mesh);

        launcherFactory
                .createLauncherFor(e2a, e2b, e2c, e2d)
                .launchProductions();


        printMatrix(a2a.m_vertex, 5, mesh.getDofsY());

        printMatrix(a2b.m_vertex, 5, mesh.getDofsY());

        printMatrix(a2c.m_vertex, 5, mesh.getDofsY());

        printMatrix(a2d.m_vertex, 5, mesh.getDofsY());

        A2_2 a22a = new A2_2(p2a.m_vertex, mesh);
        A2_2 a22b = new A2_2(p2b.m_vertex, mesh);

        launcherFactory
                .createLauncherFor(a22a, a22b)
                .launchProductions();


        printMatrix(a22a.m_vertex, 6, mesh.getDofsY());

        printMatrix(a22b.m_vertex, 6, mesh.getDofsY());

        E2_2_6 e26a = new E2_2_6(p2a.m_vertex, mesh);
        E2_2_6 e26b = new E2_2_6(p2b.m_vertex, mesh);

        launcherFactory
                .createLauncherFor(e26a, e26b)
                .launchProductions();


        printMatrix(a22a.m_vertex, 6, mesh.getDofsY());

        printMatrix(a22b.m_vertex, 6, mesh.getDofsY());

        // [Aroot]
        Aroot aroot = new Aroot(p1.m_vertex, mesh);

        launcherFactory
                .createLauncherFor(aroot)
                .launchProductions();


        printMatrix(aroot.m_vertex, 6, mesh.getDofsY());

        Eroot eroot = new Eroot(p1.m_vertex, mesh);
        launcherFactory
                .createLauncherFor(eroot)
                .launchProductions();


        printMatrix(eroot.m_vertex, 6, mesh.getDofsY());

        BS_2_6 bs1a = new BS_2_6(p2a.m_vertex, mesh);
        BS_2_6 bs1b = new BS_2_6(p2b.m_vertex, mesh);

        launcherFactory
                .createLauncherFor(bs1a, bs1b)
                .launchProductions();



        BS_1_5 bs2a = new BS_1_5(p3a1.m_vertex, mesh);
        BS_1_5 bs2b = new BS_1_5(p3a2.m_vertex, mesh);
        BS_1_5 bs2c = new BS_1_5(p3b1.m_vertex, mesh);
        BS_1_5 bs2d = new BS_1_5(p3b2.m_vertex, mesh);

        launcherFactory
                .createLauncherFor(bs2a, bs2b, bs2c, bs2d)
                .launchProductions();


        for (BS_1_5 bs : Arrays.asList(bs2a, bs2b, bs2c, bs2d)) {
            printMatrix(bs.m_vertex, 5, mesh.getDofsY());

        }

        double[][] rhs = new double[mesh.getElementsX() * 3 + mesh.getSplineOrder() + 1][];
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

        for (int i = 1; i <= mesh.getDofsX(); ++i) {
            for (int j = 1; j <= mesh.getDofsY(); ++j) {
                System.out.printf("%6.2f ", rhs[i][j]);
            }

        }

        S = new Vertex(null, null, null, null, "Sy", 0, mesh.getResolutionY(), mesh);


        P1y p1y = new P1y(S, mesh);

        launcherFactory
                .createLauncherFor(p1y)
                .launchProductions();


        logger.debug("p1y.m_vertex.m_left" + p1y.m_vertex.m_left);
        logger.debug("p1y.m_vertex.m_right" + p1y.m_vertex.m_right);
        P2 p2ay = new P2(p1y.m_vertex.m_left, mesh);
        P2 p2by = new P2(p1y.m_vertex.m_right, mesh);

        launcherFactory
                .createLauncherFor(p2ay, p2by)
                .launchProductions();



        P3 p3a1y = new P3(p2ay.m_vertex.m_left, mesh);
        P3 p3a2y = new P3(p2ay.m_vertex.m_right, mesh);
        P3 p3b1y = new P3(p2by.m_vertex.m_left, mesh);
        P3 p3b2y = new P3(p2by.m_vertex.m_right, mesh);

        launcherFactory
                .createLauncherFor(p3a1y, p3a2y, p3b1y, p3b2y)
                .launchProductions();



        A1y a1y = new A1y(p3a1y.m_vertex.m_left, rhs, new double[]{1, 1. / 2, 1. / 3}, 0, mesh);
        Ay a2y = new Ay(p3a1y.m_vertex.m_middle, rhs, new double[]{1. / 2, 1. / 3, 1. / 3}, 1, mesh);
        Ay a3y = new Ay(p3a1y.m_vertex.m_right, rhs, new double[]{1. / 3, 1. / 3, 1. / 3}, 2, mesh);
        Ay a4y = new Ay(p3a2y.m_vertex.m_left, rhs, new double[]{1. / 3, 1. / 3, 1. / 3}, 3, mesh);
        Ay a5y = new Ay(p3a2y.m_vertex.m_middle, rhs, new double[]{1. / 3, 1. / 3, 1. / 3}, 4, mesh);
        Ay a6y = new Ay(p3a2y.m_vertex.m_right, rhs, new double[]{1. / 3, 1. / 3, 1. / 3}, 5, mesh);
        Ay a7y = new Ay(p3b1y.m_vertex.m_left, rhs, new double[]{1. / 3, 1. / 3, 1. / 3}, 6, mesh);
        Ay a8y = new Ay(p3b1y.m_vertex.m_middle, rhs, new double[]{1. / 3, 1. / 3, 1. / 3}, 7, mesh);
        Ay a9y = new Ay(p3b1y.m_vertex.m_right, rhs, new double[]{1. / 3, 1. / 3, 1. / 3}, 8, mesh);
        Ay a10y = new Ay(p3b2y.m_vertex.m_left, rhs, new double[]{1. / 3, 1. / 3, 1. / 3}, 9, mesh);
        Ay a11y = new Ay(p3b2y.m_vertex.m_middle, rhs, new double[]{1. / 3, 1. / 3, 1. / 2}, 10, mesh);
        ANy a12y = new ANy(p3b2y.m_vertex.m_right, rhs, new double[]{1. / 3, 1. / 2, 1}, 11, mesh);

        launcherFactory
                .createLauncherFor(a1y, a2y, a3y, a4y, a5y, a6y, a7y, a8y, a9y, a10y, a11y, a12y)
                .launchProductions();


        printMatrix(a1y.m_vertex, 3, mesh.getDofsX());

        printMatrix(a2y.m_vertex, 3, mesh.getDofsX());

        printMatrix(a3y.m_vertex, 3, mesh.getDofsX());

        printMatrix(a4y.m_vertex, 3, mesh.getDofsX());

        printMatrix(a5y.m_vertex, 3, mesh.getDofsX());

        printMatrix(a6y.m_vertex, 3, mesh.getDofsX());

        printMatrix(a7y.m_vertex, 3, mesh.getDofsX());

        printMatrix(a8y.m_vertex, 3, mesh.getDofsX());

        printMatrix(a9y.m_vertex, 3, mesh.getDofsX());

        printMatrix(a10y.m_vertex, 3, mesh.getDofsX());

        printMatrix(a11y.m_vertex, 3, mesh.getDofsX());

        printMatrix(a12y.m_vertex, 3, mesh.getDofsX());

        a2a = new A2_3(p3a1y.m_vertex, mesh);
        a2b = new A2_3(p3a2y.m_vertex, mesh);
        a2c = new A2_3(p3b1y.m_vertex, mesh);
        a2d = new A2_3(p3b2y.m_vertex, mesh);

        launcherFactory
                .createLauncherFor(a2a, a2b, a2c, a2d)
                .launchProductions();


        printMatrix(a2a.m_vertex, 5, mesh.getDofsX());

        printMatrix(a2b.m_vertex, 5, mesh.getDofsX());

        printMatrix(a2c.m_vertex, 5, mesh.getDofsX());

        printMatrix(a2d.m_vertex, 5, mesh.getDofsX());

        e2a = new E2_1_5(p3a1y.m_vertex, mesh);
        e2b = new E2_1_5(p3a2y.m_vertex, mesh);
        e2c = new E2_1_5(p3b1y.m_vertex, mesh);
        e2d = new E2_1_5(p3b2y.m_vertex, mesh);

        launcherFactory
                .createLauncherFor(e2a, e2b, e2c, e2d)
                .launchProductions();


        printMatrix(a2a.m_vertex, 5, mesh.getDofsX());

        printMatrix(a2b.m_vertex, 5, mesh.getDofsX());

        printMatrix(a2c.m_vertex, 5, mesh.getDofsX());

        printMatrix(a2d.m_vertex, 5, mesh.getDofsX());


        a22a = new A2_2(p2ay.m_vertex, mesh);
        a22b = new A2_2(p2by.m_vertex, mesh);

        launcherFactory
                .createLauncherFor(a22a, a22b)
                .launchProductions();


        printMatrix(a22a.m_vertex, 6, mesh.getDofsY());

        printMatrix(a22b.m_vertex, 6, mesh.getDofsY());


        e26a = new E2_2_6(p2ay.m_vertex, mesh);
        e26b = new E2_2_6(p2by.m_vertex, mesh);

        launcherFactory
                .createLauncherFor(e26a, e26b)
                .launchProductions();



        printMatrix(a22a.m_vertex, 6, mesh.getDofsX());

        printMatrix(a22b.m_vertex, 6, mesh.getDofsX());

        aroot = new Aroot(p1y.m_vertex, mesh);
        launcherFactory
                .createLauncherFor(aroot)
                .launchProductions();


        printMatrix(aroot.m_vertex, 6, mesh.getDofsX());


        eroot = new Eroot(p1y.m_vertex, mesh);

        launcherFactory
                .createLauncherFor(eroot)
                .launchProductions();


        printMatrix(aroot.m_vertex, 6, mesh.getDofsX());


        bs1a = new BS_2_6(p2ay.m_vertex, mesh);
        bs1b = new BS_2_6(p2by.m_vertex, mesh);

        launcherFactory
                .createLauncherFor(bs1a, bs1b)
                .launchProductions();


        bs2a = new BS_1_5(p3a1y.m_vertex, mesh);
        bs2b = new BS_1_5(p3a2y.m_vertex, mesh);
        bs2c = new BS_1_5(p3b1y.m_vertex, mesh);
        bs2d = new BS_1_5(p3b2y.m_vertex, mesh);

        launcherFactory
                .createLauncherFor(bs2a, bs2b, bs2c, bs2d)
                .launchProductions();


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

        for (int i = 1; i <= mesh.getDofsX(); ++i) {
            for (int j = 1; j <= mesh.getDofsY(); ++j) {
                System.out.printf("%6.2f ", rhs[i][j]);
            }
        }

        return new Solution(mesh, rhs);
    }
}