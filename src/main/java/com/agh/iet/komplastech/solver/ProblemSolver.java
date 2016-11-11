package com.agh.iet.komplastech.solver;

import com.agh.iet.komplastech.solver.execution.ProductionExecutorFactory;
import com.agh.iet.komplastech.solver.productions.HorizontalProductionFactory;
import com.agh.iet.komplastech.solver.productions.ProductionFactory;
import com.agh.iet.komplastech.solver.productions.VerticalProductionFactory;
import com.agh.iet.komplastech.solver.productions.construction.P1y;
import com.agh.iet.komplastech.solver.productions.construction.P2;
import com.agh.iet.komplastech.solver.productions.construction.P3;
import com.agh.iet.komplastech.solver.productions.solution.backsubstitution.*;
import com.agh.iet.komplastech.solver.productions.solution.factorization.A2_2;
import com.agh.iet.komplastech.solver.productions.solution.factorization.A2_3;
import com.agh.iet.komplastech.solver.productions.solution.factorization.Aroot;
import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.support.Vertex;

import static com.agh.iet.komplastech.solver.support.Vertex.aVertex;

class ProblemSolver {

    private final Mesh mesh;

    private ProductionExecutorFactory launcherFactory = new ProductionExecutorFactory();

    ProblemSolver(Mesh meshData) {
        this.mesh = meshData;
    }

    Solution solveProblem() throws Exception {
        ProductionFactory horizontalProductionFactory = new HorizontalProductionFactory(mesh);
        LeafInitializer horizontalLeafInitializer = new HorizontalLeafInitializer(mesh);
        SingleDirectionSolver horizontalProblemSolver = new SingleDirectionSolver(horizontalProductionFactory, horizontalLeafInitializer, mesh);
        Solution horizontalSolution = horizontalProblemSolver.solveInHorizontalDirection();

        ProductionFactory verticalProductionFactory = new VerticalProductionFactory(mesh, horizontalSolution);
        LeafInitializer verticalLeafInitializer = new VerticalLeafInitializer(mesh, horizontalSolution);
        SingleDirectionSolver verticalProblemSolver = new SingleDirectionSolver(verticalProductionFactory, verticalLeafInitializer, mesh);

        return verticalProblemSolver.solveInHorizontalDirection();
    }

    private void solveInVerticalDirection(double[][] rhs) {
        Vertex S;
        A2_3 a2a;
        A2_3 a2b;
        A2_3 a2c;
        A2_3 a2d;
        E2_1_5 e2a;
        E2_1_5 e2b;
        E2_1_5 e2c;
        E2_1_5 e2d;
        A2_2 a22a;
        A2_2 a22b;
        E2_2_6 e26a;
        E2_2_6 e26b;
        Aroot aroot;
        Eroot eroot;
        BS_2_6 bs1a;
        BS_2_6 bs1b;
        BS_1_5 bs2a;
        BS_1_5 bs2b;
        BS_1_5 bs2c;
        BS_1_5 bs2d;

        S = aVertex()
                .withMesh(mesh)
                .withBeggining(0)
                .withEnding(mesh.getResolutionX())
                .build();

        P1y p1y = new P1y(S, mesh);

        launcherFactory
                .createLauncherFor(p1y)
                .launchProductions();


        P2 p2ay = new P2(p1y.m_vertex.leftChild, mesh);
        P2 p2by = new P2(p1y.m_vertex.rightChild, mesh);

        launcherFactory
                .createLauncherFor(p2ay, p2by)
                .launchProductions();


        P3 p3a1y = new P3(p2ay.m_vertex.leftChild, mesh);
        P3 p3a2y = new P3(p2ay.m_vertex.rightChild, mesh);
        P3 p3b1y = new P3(p2by.m_vertex.leftChild, mesh);
        P3 p3b2y = new P3(p2by.m_vertex.rightChild, mesh);

        launcherFactory
                .createLauncherFor(p3a1y, p3a2y, p3b1y, p3b2y)
                .launchProductions();


        a2a = new A2_3(p3a1y.m_vertex, mesh);
        a2b = new A2_3(p3a2y.m_vertex, mesh);
        a2c = new A2_3(p3b1y.m_vertex, mesh);
        a2d = new A2_3(p3b2y.m_vertex, mesh);

        launcherFactory
                .createLauncherFor(a2a, a2b, a2c, a2d)
                .launchProductions();

        e2a = new E2_1_5(p3a1y.m_vertex, mesh);
        e2b = new E2_1_5(p3a2y.m_vertex, mesh);
        e2c = new E2_1_5(p3b1y.m_vertex, mesh);
        e2d = new E2_1_5(p3b2y.m_vertex, mesh);

        launcherFactory
                .createLauncherFor(e2a, e2b, e2c, e2d)
                .launchProductions();


        a22a = new A2_2(p2ay.m_vertex, mesh);
        a22b = new A2_2(p2by.m_vertex, mesh);

        launcherFactory
                .createLauncherFor(a22a, a22b)
                .launchProductions();


        e26a = new E2_2_6(p2ay.m_vertex, mesh);
        e26b = new E2_2_6(p2by.m_vertex, mesh);

        launcherFactory
                .createLauncherFor(e26a, e26b)
                .launchProductions();


        aroot = new Aroot(p1y.m_vertex, mesh);
        launcherFactory
                .createLauncherFor(aroot)
                .launchProductions();


        eroot = new Eroot(p1y.m_vertex, mesh);

        launcherFactory
                .createLauncherFor(eroot)
                .launchProductions();


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
    }

}