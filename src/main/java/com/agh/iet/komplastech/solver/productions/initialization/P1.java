package com.agh.iet.komplastech.solver.productions.initialization;
/**
 * @(#)P1.java
 * @author
 * @version 1.00 2015/2/23
 */

import com.agh.iet.komplastech.solver.Mesh;
import com.agh.iet.komplastech.solver.Vertex;
import com.agh.iet.komplastech.solver.productions.Production;

import static com.agh.iet.komplastech.solver.Vertex.aVertex;

public class P1 extends Production {

    public P1(Vertex node, Mesh mesh) {
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
                        .withMesh(node.m_mesh)
                        .withBeggining(0)
                        .withEnding(node.m_mesh.getElementsX() / 2)
                        .build()
        );
    }

    private void setRightChild(Vertex node) {
        node.setRightChild(
                aVertex()
                        .withMesh(node.m_mesh)
                        .withBeggining(node.m_mesh.getElementsX() / 2)
                        .withEnding(node.m_mesh.getElementsX())
                        .build()
        );
    }

}
