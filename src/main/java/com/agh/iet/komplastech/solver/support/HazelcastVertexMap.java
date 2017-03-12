package com.agh.iet.komplastech.solver.support;

import com.agh.iet.komplastech.solver.VertexId;
import com.agh.iet.komplastech.solver.productions.Production;
import com.hazelcast.core.IMap;
import com.hazelcast.query.Predicates;

import java.util.List;

public class HazelcastVertexMap implements VertexMap {

    private final IMap<VertexId, Vertex> vertexMap;

    public HazelcastVertexMap(IMap<VertexId, Vertex> vertexMap) {
        this.vertexMap = vertexMap;
    }

    @Override
    public void executeOnVertices(Production production, VertexRange range) {
        vertexMap.executeOnEntries(new HazelcastProductionAdapter(production),
                Predicates.between("id.id", range.start(), range.end()));

//        vertexMap.executeOnEntries(new ProductionAdapter(production),
//                (Predicate<VertexId, Vertex>) mapEntry -> mapEntry.getKey().isInRange(range));
    }

    @Override
    public List<Vertex> getSortedAtLevel(int level) {
        return null;
    }


}
