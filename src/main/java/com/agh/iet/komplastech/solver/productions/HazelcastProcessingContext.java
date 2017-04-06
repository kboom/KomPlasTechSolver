package com.agh.iet.komplastech.solver.productions;

import com.agh.iet.komplastech.solver.VertexId;
import com.agh.iet.komplastech.solver.problem.Problem;
import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.support.ProcessingContextManager;
import com.agh.iet.komplastech.solver.support.ReferenceVisitor;
import com.agh.iet.komplastech.solver.support.Vertex;

import static com.agh.iet.komplastech.solver.support.CommonProcessingObject.MESH;
import static com.agh.iet.komplastech.solver.support.CommonProcessingObject.PROBLEM;

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
    public Problem getProblem() {
        return contextManager.getFromCache(PROBLEM);
    }

    @Override
    public Mesh getMesh() {
        return contextManager.getFromCache(MESH);
    }

    @Override
    public Vertex loadVertex(VertexId vertexId) {
        // hazelcastMap -> loadAll (preload) batch
        return contextManager.getVertex(vertexId);
    }

}
