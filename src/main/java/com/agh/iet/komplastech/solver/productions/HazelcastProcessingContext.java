package com.agh.iet.komplastech.solver.productions;

import com.agh.iet.komplastech.solver.VertexId;
import com.agh.iet.komplastech.solver.support.ProcessingContextManager;
import com.agh.iet.komplastech.solver.support.ReferenceVisitor;
import com.agh.iet.komplastech.solver.support.Vertex;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

public class HazelcastProcessingContext implements ProcessingContext, ReferenceVisitor {

    private final Vertex vertex;
    private final ProcessingContextManager contextManager;

    public HazelcastProcessingContext(ProcessingContextManager processingContextManager, Vertex vertex) {
        this.vertex = vertex;
        this.contextManager = processingContextManager;
    }

    @Override
    public Vertex getVertex() {
        vertex.visitReferences(this);
        return vertex;
    }

    @Override
    public void storeVertex(Vertex vertex) {
        contextManager.storeVertex(vertex);
    }

    @Override
    public void updateVertex() {
        contextManager.replaceVertex(vertex);
    }

    @Override
    public void updateVertexAndChildren() {
        updateVertex();
        contextManager.replaceVertices(vertex.getChildren());
    }

    @Override
    public Vertex loadVertex(VertexId vertexId) {
        // hazelcastMap -> loadAll (preload) batch
        return contextManager.getVertex(vertexId);
    }

}
