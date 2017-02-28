package com.agh.iet.komplastech.solver;

import java.util.function.LongFunction;

public class VertexId {

    private final long id;

    private VertexId(long id) {
        this.id = id;
    }

    public static VertexId vertexId(long id) {
        return new VertexId(id);
    }

    public VertexId transformed(LongFunction<Long> transformer) {
        return vertexId(transformer.apply(this.id));
    }

}
