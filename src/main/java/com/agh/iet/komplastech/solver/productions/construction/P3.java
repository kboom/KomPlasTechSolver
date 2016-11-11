package com.agh.iet.komplastech.solver.productions.construction;

import com.agh.iet.komplastech.solver.productions.Production;
import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.support.Vertex;

import static com.agh.iet.komplastech.solver.support.Vertex.aVertex;

public class P3 extends Production {

    public P3(Vertex node, Mesh mesh) {
        super(node, mesh);
    }

    public Vertex apply(Vertex node) {
        setLeftChild(node);
        setMiddleChild(node);
        setRightChild(node);
        return node;
    }

    private void setLeftChild(Vertex node) {
        Vertex leftChild = aVertex()
                .withMesh(node.mesh)
                .withBeggining(node.beginning)
                .withEnding(node.beginning + (node.ending - node.beginning) / 3.0)
                .build();
        node.setLeftChild(leftChild);
        leftChild.setParent(node);
    }

    private void setMiddleChild(Vertex node) {
        Vertex middleChild = aVertex()
                .withMesh(node.mesh)
                .withBeggining(node.beginning + (node.ending - node.beginning) / 3.0)
                .withEnding(node.ending - (node.ending - node.beginning) / 3.0)
                .build();
        node.setMiddleChild(middleChild);
        middleChild.setParent(node);
    }

    private void setRightChild(Vertex node) {
        Vertex rightChild = aVertex()
                .withMesh(node.mesh)
                .withBeggining(node.beginning + (node.ending - node.beginning) * 2.0 / 3.0)
                .withEnding(node.ending)
                .build();
        node.setRightChild(rightChild);
        rightChild.setParent(node);
    }

}