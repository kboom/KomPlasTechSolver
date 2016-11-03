package com.agh.iet.komplastech.solver.productions.construction;

import com.agh.iet.komplastech.solver.Mesh;
import com.agh.iet.komplastech.solver.support.Vertex;
import com.agh.iet.komplastech.solver.productions.Production;

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
        node.setLeftChild(
                aVertex()
                        .withMesh(node.mesh)
                        .withBeggining(0)
                        .withEnding(node.mesh.getElementsY() / 2.0)
                        .build()
        );
    }

    private void setRightChild(Vertex node) {
        node.setRightChild(
                aVertex()
                        .withMesh(node.mesh)
                        .withBeggining(node.mesh.getElementsY() / 2.0)
                        .withEnding(node.mesh.getElementsY())
                        .build()
        );
    }

}
