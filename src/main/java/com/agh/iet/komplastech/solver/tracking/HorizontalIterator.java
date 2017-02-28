package com.agh.iet.komplastech.solver.tracking;

import com.agh.iet.komplastech.solver.support.VertexRange;

@FunctionalInterface
public interface HorizontalIterator {

    void forRange(VertexRange vertexRange);

}
