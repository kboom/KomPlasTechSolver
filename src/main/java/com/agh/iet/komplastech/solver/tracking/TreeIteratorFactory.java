package com.agh.iet.komplastech.solver.tracking;

import com.agh.iet.komplastech.solver.VertexId;

public class TreeIteratorFactory {

    public VerticalIterator createFor(VertexId rootId, int levelCount) {
        return new VerticalIterator(rootId, levelCount);
    }

}
