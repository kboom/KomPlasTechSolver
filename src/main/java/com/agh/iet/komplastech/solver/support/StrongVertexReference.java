package com.agh.iet.komplastech.solver.support;

public class StrongVertexReference implements VertexReference {

    private final Vertex vertex;

    public StrongVertexReference(Vertex vertex) {
        this.vertex = vertex;
    }

    @Override
    public Vertex get() {
        return null;
    }

    public static VertexReference strongReferenceOf(Vertex vertex) {
        return new StrongVertexReference(vertex);
    }

}
