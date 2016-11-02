package com.agh.iet.komplastech.solver.productions;

import com.agh.iet.komplastech.solver.Mesh;
import com.agh.iet.komplastech.solver.Vertex;

import static com.agh.iet.komplastech.solver.Vertex.aVertex;

public class P1y extends Production {

    public P1y(Vertex node, Mesh mesh) {
        super(node, mesh);
    }

    Vertex apply(Vertex node) {
        setLeftChild(node);
        setRightChild(node);
        return node;
    }

    private void setLeftChild(Vertex node) {
        node.setLeftChild(
                aVertex()
                        .withMesh(node.m_mesh)
                        .withBeggining(0)
                        .withEnding(node.m_mesh.getElementsY() / 2.0)
                        .build()
        );
    }

    private void setRightChild(Vertex node) {
        node.setRightChild(
                aVertex()
                        .withMesh(node.m_mesh)
                        .withBeggining(node.m_mesh.getElementsY() / 2.0)
                        .withEnding(node.m_mesh.getElementsY())
                        .build()
        );
    }

}
