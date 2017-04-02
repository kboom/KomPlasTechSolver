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

class HazelcastProcessingContextManager implements ProcessingContextManager {

    private final HazelcastInstance hazelcastInstance;
    private final Set<Vertex> verticesToUpdate = new HashSet<>();


    HazelcastProcessingContextManager(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
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
        IMap<VertexId, Vertex> vertices = hazelcastInstance.getMap("vertices");
        return vertices.get(vertexId);
    }

    @Override
    public void flush() {
        IMap<Object, Object> verticesStore = hazelcastInstance.getMap("vertices");
        verticesStore.putAll(verticesToUpdate.stream().collect(Collectors.toMap(Vertex::getId, Function.identity())));
    }

}
