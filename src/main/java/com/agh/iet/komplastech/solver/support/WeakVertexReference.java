package com.agh.iet.komplastech.solver.support;

import com.agh.iet.komplastech.solver.VertexId;

public class WeakVertexReference implements VertexReference {

    private transient Vertex vertex;

    private final VertexId vertexId;

    public WeakVertexReference(Vertex vertex) {
        this.vertexId = vertex.getId();
        this.vertex = vertex;
    }

    @Override
    public Vertex get() {
        return vertex;
    }

    public static VertexReference weakReferenceToVertex(Vertex to) {
        return new WeakVertexReference(to);
    }

    @Override
    public void accept(ReferenceVisitor referenceVisitor) {
        if(vertex == null) {
            System.out.println("Fetching vertex " + vertexId);
            vertex = referenceVisitor.loadVertex(vertexId);
        } else {
            System.out.println("Vertex already fetched " + vertex.getId());
        }
    }

}
