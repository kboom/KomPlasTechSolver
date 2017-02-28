package com.agh.iet.komplastech.solver.tracking;

import com.agh.iet.komplastech.solver.VertexId;
import com.agh.iet.komplastech.solver.productions.Production;
import com.agh.iet.komplastech.solver.productions.initialization.Ay;
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
        horizontalIterator.forRange(
                currentLevel < leafLevel
                        ? VertexRange.forBinary(currentLevel)
                        : VertexRange.for3Ary(leafLevel)
        );
    }

    public void forEachGoingDownOnce(HorizontalIterator horizontalIterator) {
        forEachGoingDown(1, horizontalIterator);
    }

    public void onKthFromFirst(Production production, Integer... indices) {

    }

    public void onAllBetween(Ay ay, int i, int i1) {

    }

    public void onKthFromLast(Ay ay, int i) {

    }

    private void forRangeTimes(int times, HorizontalIterator horizontalIterator, IntFunction<Integer> levelModifier) {
        for (int i = 0; i < times; i++) {
            forEachStayingAt(horizontalIterator);
            currentLevel = levelModifier.apply(currentLevel);
        }
    }

    public int totalHeight() {
        return 0;
    }

}
