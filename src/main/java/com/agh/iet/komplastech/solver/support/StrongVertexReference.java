package com.agh.iet.komplastech.solver.support;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;

import java.io.IOException;

public class StrongVertexReference implements VertexReference {

    private final Vertex vertex;

    public StrongVertexReference(Vertex vertex) {
        this.vertex = vertex;
    }

    @Override
    public Vertex get() {
        return null;
    }

    @Override
    public void accept(ReferenceVisitor referenceVisitor) {

    }

    public static VertexReference strongReferenceOf(Vertex vertex) {
        return new StrongVertexReference(vertex);
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {

    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {

    }
}
