package com.agh.iet.komplastech.solver.tracking;

import com.agh.iet.komplastech.solver.VertexId;
import com.agh.iet.komplastech.solver.support.Vertex;

import java.util.List;

/**
 * Solver needs to navigate up and down the tree.
 */
public class VerticalIterator {

    private VertexId rootId;
    private List<Vertex> allLeaves;

    public void executeAtRoot(HorizontalIterator horizontalIterator) {

    }

    public void forEachGoingUp(int times, HorizontalIterator horizontalIterator) {

    }

    public void forEachGoingDown(int times, HorizontalIterator horizontalIterator) {

    }

    public void forEachStayingAt(HorizontalIterator horizontalIterator) {

    }

    public void forEachGoingDownOnce(HorizontalIterator horizontalIterator) {

    }

    /**
     * The same as @link {@link #forEachGoingDown(int, HorizontalIterator)} but explicitly starting from root.
     */
    public void forEachGoingDownTheRoot(int times, HorizontalIterator horizontalIterator) {

    }

    public List<Vertex> getSortedLeaves() {
        return allLeaves;
    }
}
