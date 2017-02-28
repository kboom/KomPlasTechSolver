package com.agh.iet.komplastech.solver.tracking;

import com.agh.iet.komplastech.solver.VertexId;

public class TreeIteratorFactory {

    public VerticalIterator createFor(VertexId id) {
        return new VerticalIterator(id);
    }

}
