package com.agh.iet.komplastech.solver.support;

import com.agh.iet.komplastech.solver.VertexId;

import java.util.function.Function;

public final class VertexReferenceFunctionAdapter implements Function<VertexId, VertexReference> {

    private final VertexRegionMapper regionMapper;

    public VertexReferenceFunctionAdapter(VertexRegionMapper regionMapper) {
        this.regionMapper = regionMapper;
    }

    // based on id assign proper region (probably exact same region mapper should also be used in branching productions...)
    @Override
    public VertexReference apply(VertexId vertexId) {
        RegionId regionId = regionMapper.getRegionFor(vertexId);
        return new WeakVertexReference(vertexId, regionId);
    }

    public static VertexReferenceFunctionAdapter toVertexReferenceUsing(VertexRegionMapper vertexRegionMapper) {
        return new VertexReferenceFunctionAdapter(vertexRegionMapper);
    }

}
