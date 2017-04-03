package com.agh.iet.komplastech.solver.support;

import com.agh.iet.komplastech.solver.VertexId;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;

import java.io.IOException;

import static com.agh.iet.komplastech.solver.factories.HazelcastGeneralFactory.GENERAL_FACTORY_ID;
import static com.agh.iet.komplastech.solver.factories.HazelcastGeneralFactory.WEAK_VERTEX_REFERENCE;

public class WeakVertexReference implements VertexReference {

    private transient Vertex vertex;

    private VertexId vertexId;

    @SuppressWarnings("unused")
    public WeakVertexReference() {

    }

    public WeakVertexReference(Vertex vertex) {
        this.vertexId = vertex.getVertexId();
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

    @Override
    public int getFactoryId() {
        return GENERAL_FACTORY_ID;
    }

    @Override
    public int getId() {
        return WEAK_VERTEX_REFERENCE;
    }

}
