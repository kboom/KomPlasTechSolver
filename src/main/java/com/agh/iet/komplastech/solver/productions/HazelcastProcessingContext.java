package com.agh.iet.komplastech.solver.productions;

import com.agh.iet.komplastech.solver.VertexId;
import com.agh.iet.komplastech.solver.support.ReferenceVisitor;
import com.agh.iet.komplastech.solver.support.Vertex;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

public class HazelcastProcessingContext implements ProcessingContext, ReferenceVisitor {

    private final Vertex vertex;
    private final IMap<VertexId, Vertex> vertices;

    public HazelcastProcessingContext(HazelcastInstance hazelcastInstance, Vertex vertex) {
        this.vertex = vertex;
        this.vertices = hazelcastInstance.getMap("vertices");
    }

    @Override
    public Vertex getVertex() {
        vertex.visitReferences(this);
        return vertex;
    }

    @Override
    public void storeVertex(Vertex vertex) {
        vertices.put(vertex.getId(), vertex);
    }

    @Override
    public Vertex loadVertex(VertexId vertexId) {
        return vertices.get(vertexId);
    }

}
