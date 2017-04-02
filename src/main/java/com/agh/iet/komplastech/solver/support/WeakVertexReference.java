package com.agh.iet.komplastech.solver.support;

import com.agh.iet.komplastech.solver.VertexId;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;

import java.io.IOException;

public class WeakVertexReference implements VertexReference {

    private transient Vertex vertex;

    private VertexId vertexId;

    @SuppressWarnings("unused")
    public WeakVertexReference() {

    }

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
        if (vertex == null) {
            vertex = referenceVisitor.loadVertex(vertexId);
        }
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeObject(vertexId);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        vertexId = in.readObject();
    }

}
