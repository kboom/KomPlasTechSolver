package com.agh.iet.komplastech.solver.tracking;

import com.agh.iet.komplastech.solver.VertexId;
import com.agh.iet.komplastech.solver.support.VertexRange;
import com.hazelcast.util.function.IntFunction;

/**
 * Solver needs to navigate up and down the tree.
 */
public class VerticalIterator {

    private final VertexId rootId;

    private int currentLevel;
    private int leafLevel;

    public VerticalIterator(VertexId rootId, int leafLevel) {
        this.rootId = rootId;
        currentLevel = 0;
        this.leafLevel = leafLevel;
    }

    public void executeOnRootGoingDown(HorizontalIterator horizontalIterator) {
        horizontalIterator.forRange(VertexRange.unitary(rootId));
        currentLevel++;
    }

    public void forEachGoingUp(int times, HorizontalIterator horizontalIterator) {
        forRangeTimes(times, horizontalIterator, (level) -> level - 1);
    }

    public void forEachGoingDown(int times, HorizontalIterator horizontalIterator) {
        forRangeTimes(times, horizontalIterator, (level) -> level + 1);
    }

    public void forEachStayingAt(HorizontalIterator horizontalIterator) {
        horizontalIterator.forRange(getCurrentRange());
    }

    public void forEachGoingUpOnce(HorizontalIterator horizontalIterator) {
        forEachGoingUp(1, horizontalIterator);
    }

    public void forEachGoingDownOnce(HorizontalIterator horizontalIterator) {
        forEachGoingDown(1, horizontalIterator);
    }

    public void onKthLeafFromFirst(HorizontalIterator horizontalIterator, Integer offset) {
        horizontalIterator.forRange(getCurrentRange().fromLeft(offset));
    }

    public void onKthLeafFromLast(HorizontalIterator horizontalIterator, Integer offset) {
        horizontalIterator.forRange(getCurrentRange().fromRight(offset));
    }

    public void onAllLeavesBetween(HorizontalIterator horizontalIterator, int leftOffset, int rightOffset) {
        horizontalIterator.forRange(getCurrentRange().shrinkBy(leftOffset - 1, rightOffset - 1));
    }

    public VertexRange getCurrentRange() {
        return currentLevel < leafLevel
                ? VertexRange.forBinary(currentLevel)
                : VertexRange.forBinaryAndLastLevel(leafLevel, 3.0 / 2.0);
    }

    private void forRangeTimes(int times, HorizontalIterator horizontalIterator, IntFunction<Integer> levelModifier) {
        for (int i = 0; i < times; i++) {
            forEachStayingAt(horizontalIterator);
            currentLevel = levelModifier.apply(currentLevel);
        }
    }

    public void moveOneUp() {
        currentLevel--;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }
}
