package com.agh.iet.komplastech.solver;

import com.agh.iet.komplastech.solver.support.VertexRange;

import java.util.function.LongFunction;

import static java.lang.Math.floor;

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

    public boolean isInRange(VertexRange range) {
        return false;
    }

    public int relativeToCurrentLevel() {
        return (int) (id - Math.pow(2, floor(Math.log(id))));
    }

}
