package com.agh.iet.komplastech.solver.tracking;

import com.agh.iet.komplastech.solver.VertexId;
import com.agh.iet.komplastech.solver.productions.Production;
import com.agh.iet.komplastech.solver.productions.initialization.Ay;
import com.agh.iet.komplastech.solver.support.Vertex;
import com.agh.iet.komplastech.solver.support.VertexIdListProxy;
import com.hazelcast.core.EntryEvent;
import com.hazelcast.map.listener.EntryAddedListener;
import com.hazelcast.map.listener.MapListener;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.Collections.singleton;

/**
 * Solver needs to navigate up and down the tree.
 */
public class VerticalIterator implements EntryAddedListener<VertexId, Vertex> {

    private final VertexId rootId;
    private List<Vertex> allLeaves;

    private Direction direction;
    private int currentLevel;
    private int levelCount;

    private final VertexIdListProxy vertexIdListProxy;

    public VerticalIterator(VertexId rootId, VertexIdListProxy vertexIdListProxy) {
        this.rootId = rootId;
        this.vertexIdListProxy = vertexIdListProxy;
        direction = Direction.STAY;
        currentLevel = 0;
        levelCount = 0;
    }

    public void executeOnRootGoingDown(HorizontalIterator horizontalIterator) {
        direction = Direction.DOWN;
        horizontalIterator.forEach(singleton(rootId));
        currentLevel++;
        direction = Direction.STAY;
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

    public void forAllAtLevelStartingFrom(Production production, Integer... indices) {

    }

    public void forAllBetween(Ay ay, int i, int i1) {

    }

    @Override
    public void entryAdded(EntryEvent<VertexId, Vertex> event) {
        switch(direction) {
            case DOWN:
                if(currentLevel < levelCount) {
                    vertexIdListProxy.add(currentLevel, event.getKey());
                }
                break;
            case UP:
            case STAY:
                throw new IllegalStateException("Did not expect to grow a tree");
        }
    }

    private enum Direction {
        UP,
        DOWN,
        STAY
    }

}
