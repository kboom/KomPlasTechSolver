package com.agh.iet.komplastech.solver.support;

import com.agh.iet.komplastech.solver.VertexId;
import com.agh.iet.komplastech.solver.productions.HazelcastProcessingContext;
import com.agh.iet.komplastech.solver.productions.ProcessingContext;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.agh.iet.komplastech.solver.support.VertexReferenceFunctionAdapter.toVertexReferenceUsing;

class HazelcastProcessingContextManager implements ProcessingContextManager {

    private final Set<Vertex> verticesToUpdate = new HashSet<>();
    private final VertexRegionMapper vertexRegionMapper;

    private final IMap<VertexReference, Vertex> vertices;
    private final IMap<CommonProcessingObject, Object> commons;


    HazelcastProcessingContextManager(HazelcastInstance hazelcastInstance,
                                      VertexRegionMapper vertexRegionMapper) {
        this.vertexRegionMapper = vertexRegionMapper;

        vertices = hazelcastInstance.getMap("vertices");
        commons = hazelcastInstance.getMap("commons");
    }

    ProcessingContext createFor(Vertex vertex) {
        return new HazelcastProcessingContext(this, vertex);
    }

    @Override
    public void replaceVertices(Collection<Vertex> children) {
        verticesToUpdate.addAll(children);
    }

    @Override
    public void replaceVertex(Vertex vertex) {
        verticesToUpdate.add(vertex);
    }

    @Override
    public void storeVertex(Vertex vertex) {
        verticesToUpdate.add(vertex);
    }

    @Override
    public Vertex getVertex(VertexId vertexId) {
        return vertices.get(toVertexReferenceUsing(vertexRegionMapper).apply(vertexId));
    }

    @Override
    public void flush() {
        vertices.putAll(verticesToUpdate.stream().collect(Collectors.toMap(
                Vertex::getVertexReference, Function.identity())));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getFromCache(CommonProcessingObject id) {
        return (T) commons.get(id);
    }

}