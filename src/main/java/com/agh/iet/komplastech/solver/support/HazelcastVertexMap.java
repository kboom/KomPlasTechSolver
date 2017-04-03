package com.agh.iet.komplastech.solver.support;

import com.agh.iet.komplastech.solver.VertexId;
import com.agh.iet.komplastech.solver.productions.Production;
import com.hazelcast.core.IMap;

import java.util.List;
import java.util.stream.Collectors;

public class HazelcastVertexMap implements VertexMap {

    private final IMap<VertexId, Vertex> vertexMap;

    public HazelcastVertexMap(IMap<VertexId, Vertex> vertexMap) {
        this.vertexMap = vertexMap;
    }

    @Override
    public void executeOnVertices(Production production, VertexRange range) {
//        vertexMap.executeOnEntries(new HazelcastProductionAdapter(production, null),
//                Predicates.between("id.id", range.start(), range.end()));

//        vertexMap.executeOnEntries(new ProductionAdapter(production),
//                (Predicate<VertexId, Vertex>) mapEntry -> mapEntry.getKey().isInRange(range));
    }

    @Override
    public List<Vertex> getAllInRange(VertexRange vertexRange) {
        return vertexMap.getAll(vertexRange.getVerticesInRange()
                .stream().collect(Collectors.toSet())).values()
                .stream().sorted((v1, v2) ->
                        v1.getVertexId().getAbsoluteIndex() - v2.getVertexId().getAbsoluteIndex()
                ).collect(Collectors.toList());
    }


}
