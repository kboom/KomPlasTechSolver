package com.agh.iet.komplastech.solver;

import java.io.Serializable;
import java.util.function.IntFunction;

public class VertexId implements Serializable {

    private final int id;

    private VertexId(int id) {
        this.id = id;
    }

    public static VertexId vertexId(int id) {
        return new VertexId(id);
    }

    public VertexId transformed(IntFunction<Integer> transformer) {
        return vertexId(transformer.apply(this.id));
    }

    public int getAbsoluteIndex() {
        return id;
    }

    @Override
    public String toString() {
        return "VertexId{" +
                "id=" + id +
                '}';
    }
}
