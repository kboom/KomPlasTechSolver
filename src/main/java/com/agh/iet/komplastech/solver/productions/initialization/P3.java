package com.agh.iet.komplastech.solver.productions.initialization;

import com.agh.iet.komplastech.solver.Mesh;
import com.agh.iet.komplastech.solver.Vertex;
import com.agh.iet.komplastech.solver.productions.Production;

import static com.agh.iet.komplastech.solver.Vertex.aVertex;

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
        node.setLeftChild(
                aVertex()
                        .withMesh(node.m_mesh)
                        .withBeggining(node.m_beg)
                        .withEnding(node.m_beg + (node.m_end - node.m_beg) / 3.0)
                        .build()
        );
    }

    private void setMiddleChild(Vertex node) {
        node.setMiddleChild(
                aVertex()
                        .withMesh(node.m_mesh)
                        .withBeggining(node.m_beg + (node.m_end - node.m_beg) / 3.0)
                        .withEnding(node.m_end - (node.m_end - node.m_beg) / 3.0)
                        .build()
        );
    }

    private void setRightChild(Vertex node) {
        node.setRightChild(
                aVertex()
                        .withMesh(node.m_mesh)
                        .withBeggining(node.m_beg + (node.m_end - node.m_beg) * 2.0 / 3.0)
                        .withEnding(node.m_end)
                        .build()
        );
    }

}