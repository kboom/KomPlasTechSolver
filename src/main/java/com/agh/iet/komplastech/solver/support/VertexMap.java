package com.agh.iet.komplastech.solver.support;

import com.agh.iet.komplastech.solver.VertexId;
import com.agh.iet.komplastech.solver.productions.Production;

import java.util.Set;

public interface VertexMap {

    /**
     * todo: later think of composite productions - launch two at once - merging and elimination in one - ProductionSequence or sth cause this would save some cycles
     * @param vertexIdSet
     * @param production
     */
    void executeOnVertices(Set<VertexId> vertexIdSet, Production production);

}
