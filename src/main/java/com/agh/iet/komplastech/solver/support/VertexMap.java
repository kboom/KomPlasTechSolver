package com.agh.iet.komplastech.solver.support;

import com.agh.iet.komplastech.solver.productions.Production;

import java.util.List;

public interface VertexMap {

    /**
     * todo: later think of composite productions - launch two at once - merging and elimination in one - ProductionSequence or sth cause this would save some cycles
     *
     * @param production
     */
    void executeOnVertices(Production production, VertexRange range);

    List<Vertex> getSortedAtLevel(int level);
}
