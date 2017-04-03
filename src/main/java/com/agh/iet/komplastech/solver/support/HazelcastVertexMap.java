package com.agh.iet.komplastech.solver.support;

import com.hazelcast.core.IMap;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.agh.iet.komplastech.solver.support.VertexReferenceFunctionAdapter.toVertexReferenceUsing;

public class HazelcastVertexMap implements VertexMap {

    private final IMap<VertexReference, Vertex> vertexMap;

    private final VertexRegionMapper vertexRegionMapper;

    public HazelcastVertexMap(IMap<VertexReference, Vertex> vertexMap, VertexRegionMapper vertexRegionMapper) {
        this.vertexMap = vertexMap;
        this.vertexRegionMapper = vertexRegionMapper;
    }

    @Override
    public List<Vertex> getAllInRange(VertexRange vertexRange) {
        return vertexMap.getAll(getVertexReferencesFor(vertexRange))
                .values()
                .stream()
                .sorted((v1, v2) ->
                        v1.getVertexId().getAbsoluteIndex() - v2.getVertexId().getAbsoluteIndex()
                ).collect(Collectors.toList());
    }

    private Set<VertexReference> getVertexReferencesFor(VertexRange vertexRange) {
        return vertexRange.getVerticesInRange()
                .stream()
                .map(toVertexReferenceUsing(vertexRegionMapper))
                .collect(Collectors.toSet());
    }


}
