package com.agh.iet.komplastech.solver;

import com.agh.iet.komplastech.solver.productions.Production;
import com.agh.iet.komplastech.solver.productions.initialization.A1y;
import com.agh.iet.komplastech.solver.productions.initialization.ANy;
import com.agh.iet.komplastech.solver.productions.initialization.Ay;
import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.support.Vertex;

import java.util.ArrayList;
import java.util.List;

class VerticalLeafInitializer implements LeafInitializer {

    private final Mesh mesh;

    private final Solution horizontalSolution;

    VerticalLeafInitializer(Mesh mesh, Solution horizontalSolution) {
        this.mesh = mesh;
        this.horizontalSolution = horizontalSolution;
    }

    @Override
    public List<Production> initializeLeaves(List<Vertex> leafLevelVertices) {
        final int leafCount = leafLevelVertices.size();
        double[][] rhs = horizontalSolution.getRhs();
        List<Production> initializationProductions = new ArrayList<>(leafLevelVertices.size());

        Vertex firstVertex = leafLevelVertices.get(0);
        initializationProductions.add(new A1y(firstVertex.leftChild, rhs, new double[]{1, 1. / 2, 1. / 3}, 0, mesh));
        initializationProductions.add(new Ay(firstVertex.middleChild, rhs, new double[]{1. / 2, 1. / 3, 1. / 3}, 1, mesh));
        initializationProductions.add(new Ay(firstVertex.rightChild, rhs, new double[]{1. / 3, 1. / 3, 1. / 3}, 2, mesh));

        for (int i = 1; i < leafCount - 1; i++) {
            Vertex vertex = leafLevelVertices.get(i);
            initializationProductions.add(new Ay(vertex.leftChild, rhs, new double[]{1. / 3, 1. / 3, 1. / 3}, 3 * i, mesh));
            initializationProductions.add(new Ay(vertex.middleChild, rhs, new double[]{1. / 3, 1. / 3, 1. / 3}, 3 * i + 1, mesh));
            initializationProductions.add(new Ay(vertex.rightChild, rhs, new double[]{1. / 3, 1. / 3, 1. / 3}, 3 * i + 2, mesh));
        }


        Vertex lastVertex = leafLevelVertices.get(leafCount - 1);
        initializationProductions.add(new Ay(lastVertex.leftChild, rhs, new double[]{1. / 3, 1. / 3, 1. / 3}, leafCount * 3 - 3, mesh));
        initializationProductions.add(new Ay(lastVertex.middleChild, rhs, new double[]{1. / 3, 1. / 3, 1. / 2}, leafCount * 3 - 2, mesh));
        initializationProductions.add(new ANy(lastVertex.rightChild, rhs, new double[]{1. / 3, 1. / 2, 1}, leafCount * 3 - 1, mesh));

        return initializationProductions;
    }

}
