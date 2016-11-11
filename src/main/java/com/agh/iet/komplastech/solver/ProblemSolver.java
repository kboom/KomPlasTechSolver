package com.agh.iet.komplastech.solver;

import com.agh.iet.komplastech.solver.execution.ProductionExecutorFactory;
import com.agh.iet.komplastech.solver.productions.Production;
import com.agh.iet.komplastech.solver.productions.construction.P1;
import com.agh.iet.komplastech.solver.productions.construction.P1y;
import com.agh.iet.komplastech.solver.productions.construction.P2;
import com.agh.iet.komplastech.solver.productions.construction.P3;
import com.agh.iet.komplastech.solver.productions.initialization.*;
import com.agh.iet.komplastech.solver.productions.solution.backsubstitution.*;
import com.agh.iet.komplastech.solver.productions.solution.factorization.A2_2;
import com.agh.iet.komplastech.solver.productions.solution.factorization.A2_3;
import com.agh.iet.komplastech.solver.productions.solution.factorization.Aroot;
import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.support.Vertex;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static com.agh.iet.komplastech.solver.support.Vertex.aVertex;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

class ProblemSolver {

    private static final int ROOT_LEVEL_HEIGHT = 1;
    private static final int LEAF_LEVEL_HEIGHT = 1;

    private Logger logger = Logger.getLogger(ProblemSolver.class);

    List<Vertex> lastLevelVertices;
    List<Vertex> leafLevelVertices;

    private final Mesh mesh;

    private ProductionExecutorFactory launcherFactory = new ProductionExecutorFactory();

    ProblemSolver(Mesh meshData) {
        this.mesh = meshData;
    }

    Solution solveProblem() throws Exception {
        solveInHorizontalDirection();
        double[][] rhs = getRightHandSideForVerticalDirection();
        solveInVerticalDirection(rhs);
        return new Solution(mesh, rhs);
    }

    private double[][] getRightHandSideForVerticalDirection() {
        // fixed for now
        double[][] rhs = new double[mesh.getElementsX() * 3 + mesh.getSplineOrder() + 1][];
        Vertex a = lastLevelVertices.get(0);
        Vertex b = lastLevelVertices.get(1);
        Vertex c = lastLevelVertices.get(2);
        Vertex d = lastLevelVertices.get(3);

        rhs[1] = a.m_x[1];
        rhs[2] = a.m_x[2];
        rhs[3] = a.m_x[3];
        rhs[4] = a.m_x[4];
        rhs[5] = a.m_x[5];
        rhs[6] = b.m_x[3];
        rhs[7] = b.m_x[4];
        rhs[8] = b.m_x[5];
        rhs[9] = c.m_x[3];
        rhs[10] = c.m_x[4];
        rhs[11] = c.m_x[5];
        rhs[12] = d.m_x[3];
        rhs[13] = d.m_x[4];
        rhs[14] = d.m_x[5];
        return rhs;
    }

    private int log2(double value) {
        return (int) Math.floor(Math.log(value) / Math.log(2));
    }

    private void solveInHorizontalDirection() {
        P1 p1 = createRoot();
        List<Production> previousLevelProductions = buildIntermediateLevels(p1);
        buildLeaves(previousLevelProductions);
        initializeLeaves(previousLevelProductions);
        mergeLeaves(previousLevelProductions);
        eliminateLeaves(previousLevelProductions);
        factorizeTree(previousLevelProductions);
        solveRoot(p1);
        lastLevelVertices = backwardSubstituteIntermediate(p1);
        leafLevelVertices = collectChildren(lastLevelVertices);
        backwardSubstituteLeaves(leafLevelVertices);
    }

    private void backwardSubstituteLeaves(List<Vertex> leafLevelVertices) {
        launcherFactory
                .createLauncherFor(
                        leafLevelVertices.stream().map(vertex
                                -> new BS_1_5(vertex, mesh))
                                .collect(toList())
                )
                .launchProductions();
    }

    private List<Vertex> backwardSubstituteIntermediate(P1 p1) {
        List<Vertex> verticesAtLevel = p1.m_vertex.getChildren();
        for (int level = 0; level < getIntermediateLevelsCount(); level++) {
            launcherFactory
                    .createLauncherFor(
                            verticesAtLevel.stream().map(vertex
                                    -> new BS_2_6(vertex, mesh))
                                    .collect(toList())
                    )
                    .launchProductions();

            verticesAtLevel = collectChildren(verticesAtLevel);
        }
        return verticesAtLevel;
    }

    private List<Vertex> collectChildren(List<Vertex> parents) {
        List<Vertex> childVertices = new ArrayList<>();
        for (Vertex vertex : parents) {
            childVertices.addAll(vertex.getChildren());
        }
        return childVertices;
    }

    private P1 createRoot() {
        Vertex S = aVertex()
                .withMesh(mesh)
                .withBeggining(0)
                .withEnding(mesh.getResolutionX())
                .build();


        P1 p1 = new P1(S, mesh);

        launcherFactory
                .createLauncherFor(p1)
                .launchProductions();
        return p1;
    }

    private void factorizeTree(List<Production> previousLevelProductions) {
        List<Vertex> verticesAtLevel = previousLevelProductions.stream()
                .map(production -> production.m_vertex).collect(toList());

        while (verticesAtLevel.size() > 1) {
            launcherFactory
                    .createLauncherFor(
                            verticesAtLevel.stream().map(vertex
                                    -> new A2_2(vertex, mesh))
                                    .collect(toList())
                    )
                    .launchProductions();

            launcherFactory
                    .createLauncherFor(
                            verticesAtLevel.stream().map(vertex
                                    -> new E2_2_6(vertex, mesh))
                                    .collect(toList())
                    )
                    .launchProductions();

            verticesAtLevel = collectParents(verticesAtLevel);
        }
    }

    private void solveRoot(P1 p1) {
        Aroot aroot = new Aroot(p1.m_vertex, mesh);

        launcherFactory
                .createLauncherFor(aroot)
                .launchProductions();

        Eroot eroot = new Eroot(p1.m_vertex, mesh);
        launcherFactory
                .createLauncherFor(eroot)
                .launchProductions();
    }

    private List<Vertex> collectParents(List<Vertex> verticesAtLevel) {
        List<Vertex> parentVertices = new ArrayList<>();
        for (Vertex vertex : verticesAtLevel) {
            Vertex parentVertex = vertex.getParent();
            if (!parentVertices.contains(parentVertex)) {
                parentVertices.add(parentVertex);
            }
        }
        return parentVertices;
    }

    private void eliminateLeaves(List<Production> previousLevelProductions) {
        launcherFactory
                .createLauncherFor(previousLevelProductions.stream().map(leafLevelProduction
                        -> new E2_1_5(leafLevelProduction.m_vertex, mesh))
                        .collect(toList()))
                .launchProductions();
    }

    private void mergeLeaves(List<Production> previousLevelProductions) {
        launcherFactory
                .createLauncherFor(previousLevelProductions.stream().map(leafLevelProduction
                        -> new A2_3(leafLevelProduction.m_vertex, mesh))
                        .collect(toList()))
                .launchProductions();
    }

    private void initializeLeaves(List<Production> previousLevelProductions) {
        List<Production> initializationProductions = new ArrayList<>(previousLevelProductions.size());

        Production firstProduction = previousLevelProductions.get(0);
        initializationProductions.add(new A1(firstProduction.m_vertex.leftChild, mesh));
        initializationProductions.add(new A(firstProduction.m_vertex.middleChild, mesh));
        initializationProductions.add(new A(firstProduction.m_vertex.rightChild, mesh));


        for (int i = 1; i < previousLevelProductions.size() - 2; i++) {
            Production production = previousLevelProductions.get(0);
            initializationProductions.add(new A(production.m_vertex.leftChild, mesh));
            initializationProductions.add(new A(production.m_vertex.middleChild, mesh));
            initializationProductions.add(new A(production.m_vertex.rightChild, mesh));
        }


        Production lastProduction = previousLevelProductions.get(previousLevelProductions.size() - 1);
        initializationProductions.add(new A(lastProduction.m_vertex.leftChild, mesh));
        initializationProductions.add(new A(lastProduction.m_vertex.middleChild, mesh));
        initializationProductions.add(new AN(lastProduction.m_vertex.rightChild, mesh));

        launcherFactory
                .createLauncherFor(initializationProductions)
                .launchProductions();
    }

    private List<Production> buildIntermediateLevels(P1 p1) {
        List<Production> previousLevelProductions = singletonList(p1);
        int intermediateTreeLevelCount = getIntermediateLevelsCount();
        for (int i = 0; i < intermediateTreeLevelCount; i++) {
            int elementsAtPrevious = (int) Math.pow(2, i);
            List<Production> newLevelProductions = new ArrayList<>(2 * elementsAtPrevious);
            for (int j = 0; j < elementsAtPrevious; j++) {
                Production previousProduction = previousLevelProductions.get(j);
                Vertex previousVertex = previousProduction.m_vertex;
                P2 leftChildProduction = new P2(previousVertex.leftChild, mesh);
                newLevelProductions.add(leftChildProduction);
                P2 rightChildProduction = new P2(previousVertex.rightChild, mesh);
                newLevelProductions.add(rightChildProduction);
            }
            previousLevelProductions = newLevelProductions;
        }

        launcherFactory
                .createLauncherFor(previousLevelProductions)
                .launchProductions();
        return previousLevelProductions;
    }

    private int getIntermediateLevelsCount() {
        return log2(mesh.getElementsX()) - ROOT_LEVEL_HEIGHT - LEAF_LEVEL_HEIGHT;
    }

    private void buildLeaves(List<Production> previousLevelProductions) {
        int leafLevelCount = previousLevelProductions.size();
        List<Production> leafInitializationProductions = new ArrayList<>(leafLevelCount);
        for (Production previousProduction : previousLevelProductions) {
            Vertex previousVertex = previousProduction.m_vertex;
            P3 leftChildProduction = new P3(previousVertex.leftChild, mesh);
            leafInitializationProductions.add(leftChildProduction);
            P3 rightChildProduction = new P3(previousVertex.rightChild, mesh);
            leafInitializationProductions.add(rightChildProduction);
        }

        launcherFactory
                .createLauncherFor(leafInitializationProductions)
                .launchProductions();
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


        A1y a1y = new A1y(p3a1y.m_vertex.leftChild, rhs, new double[]{1, 1. / 2, 1. / 3}, 0, mesh);
        Ay a2y = new Ay(p3a1y.m_vertex.middleChild, rhs, new double[]{1. / 2, 1. / 3, 1. / 3}, 1, mesh);
        Ay a3y = new Ay(p3a1y.m_vertex.rightChild, rhs, new double[]{1. / 3, 1. / 3, 1. / 3}, 2, mesh);
        Ay a4y = new Ay(p3a2y.m_vertex.leftChild, rhs, new double[]{1. / 3, 1. / 3, 1. / 3}, 3, mesh);
        Ay a5y = new Ay(p3a2y.m_vertex.middleChild, rhs, new double[]{1. / 3, 1. / 3, 1. / 3}, 4, mesh);
        Ay a6y = new Ay(p3a2y.m_vertex.rightChild, rhs, new double[]{1. / 3, 1. / 3, 1. / 3}, 5, mesh);
        Ay a7y = new Ay(p3b1y.m_vertex.leftChild, rhs, new double[]{1. / 3, 1. / 3, 1. / 3}, 6, mesh);
        Ay a8y = new Ay(p3b1y.m_vertex.middleChild, rhs, new double[]{1. / 3, 1. / 3, 1. / 3}, 7, mesh);
        Ay a9y = new Ay(p3b1y.m_vertex.rightChild, rhs, new double[]{1. / 3, 1. / 3, 1. / 3}, 8, mesh);
        Ay a10y = new Ay(p3b2y.m_vertex.leftChild, rhs, new double[]{1. / 3, 1. / 3, 1. / 3}, 9, mesh);
        Ay a11y = new Ay(p3b2y.m_vertex.middleChild, rhs, new double[]{1. / 3, 1. / 3, 1. / 2}, 10, mesh);
        ANy a12y = new ANy(p3b2y.m_vertex.rightChild, rhs, new double[]{1. / 3, 1. / 2, 1}, 11, mesh);

        launcherFactory
                .createLauncherFor(a1y, a2y, a3y, a4y, a5y, a6y, a7y, a8y, a9y, a10y, a11y, a12y)
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

}