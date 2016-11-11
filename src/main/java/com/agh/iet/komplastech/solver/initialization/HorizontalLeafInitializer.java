package com.agh.iet.komplastech.solver.initialization;

import com.agh.iet.komplastech.solver.productions.Production;
import com.agh.iet.komplastech.solver.productions.initialization.A;
import com.agh.iet.komplastech.solver.productions.initialization.A1;
import com.agh.iet.komplastech.solver.productions.initialization.AN;
import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.support.Vertex;

import java.util.ArrayList;
import java.util.List;

public class HorizontalLeafInitializer implements LeafInitializer {

    private Mesh mesh;

    public HorizontalLeafInitializer(Mesh mesh) {
        this.mesh = mesh;
    }

    @Override
    public List<Production> initializeLeaves(List<Vertex> leafLevelVertices) {
        List<Production> initializationProductions = new ArrayList<>(leafLevelVertices.size());

        Vertex firstVertex = leafLevelVertices.get(0);
        initializationProductions.add(new A1(firstVertex.leftChild, mesh));
        initializationProductions.add(new A(firstVertex.middleChild, mesh));
        initializationProductions.add(new A(firstVertex.rightChild, mesh));


        for (int i = 1; i < leafLevelVertices.size() - 1; i++) {
            Vertex vertex = leafLevelVertices.get(i);
            initializationProductions.add(new A(vertex.leftChild, mesh));
            initializationProductions.add(new A(vertex.middleChild, mesh));
            initializationProductions.add(new A(vertex.rightChild, mesh));
        }


        Vertex lastVertex = leafLevelVertices.get(leafLevelVertices.size() - 1);
        initializationProductions.add(new A(lastVertex.leftChild, mesh));
        initializationProductions.add(new A(lastVertex.middleChild, mesh));
        initializationProductions.add(new AN(lastVertex.rightChild, mesh));

        return initializationProductions;
    }


}
