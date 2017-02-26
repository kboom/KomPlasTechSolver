package com.agh.iet.komplastech.solver.productions.construction;

import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.support.Vertex;
import com.agh.iet.komplastech.solver.productions.Production;

import static com.agh.iet.komplastech.solver.support.Vertex.aVertex;

public class P2 extends Production {

    public P2(Vertex node, Mesh mesh) {
        super(node, mesh);
    }

    public Vertex apply(Vertex node) {
        setLeftChild(node);
        setRightChild(node);
        return node;
    }

    private void setLeftChild(Vertex node) {
        Vertex leftChild = aVertex(vertexGenerator.newId())
                .withMesh(node.mesh)
                .withBeggining(node.beginning)
                .withEnding(node.beginning + (node.ending - node.beginning) * 0.5)
                .build();
        node.setLeftChild(leftChild);
        leftChild.setParent(node);
    }

    private void setRightChild(Vertex node) {
        Vertex rightChild = aVertex(vertexGenerator.newId())
                .withMesh(node.mesh)
                .withBeggining(node.beginning + (node.ending - node.beginning) * 0.5)
                .withEnding(node.ending)
                .build();
        node.setRightChild(rightChild);
        rightChild.setParent(node);
    }

}
