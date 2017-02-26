package com.agh.iet.komplastech.solver.productions.construction;

import com.agh.iet.komplastech.solver.productions.Production;
import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.support.Vertex;

import static com.agh.iet.komplastech.solver.support.Vertex.aVertex;

public class P1y extends Production {

    public P1y(Vertex node, Mesh mesh) {
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
                .withBeggining(0)
                .withEnding(node.mesh.getElementsY() / 2.0)
                .build();
        node.setLeftChild(leftChild);
        leftChild.setParent(node);
    }

    private void setRightChild(Vertex node) {
        Vertex rightChild = aVertex(vertexGenerator.newId())
                .withMesh(node.mesh)
                .withBeggining(node.mesh.getElementsY() / 2.0)
                .withEnding(node.mesh.getElementsY())
                .build();
        node.setRightChild(rightChild);
        rightChild.setParent(node);
    }

}
