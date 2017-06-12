package com.agh.iet.komplastech.solver.productions;

import com.agh.iet.komplastech.solver.Solution;
import com.agh.iet.komplastech.solver.problem.Problem;
import com.agh.iet.komplastech.solver.support.*;

import static com.agh.iet.komplastech.solver.support.CommonProcessingObject.*;

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
    public ComputeConfig getComputeConfig() {
        return contextManager.getFromCache(COMPUTE_CONFIG);
    }

    @Override
    public Mesh getMesh() {
        return contextManager.getFromCache(MESH);
    }

    @Override
    public Solution getSolution() {
        return contextManager.getFromCache(SOLUTION);
    }

    @Override
    public VertexRegionMapper getRegionMapper() {
        return new VertexRegionMapper(getMesh(), getComputeConfig());
    }

    @Override
    public Vertex loadVertex(WeakVertexReference reference) {
        // hazelcastMap -> loadAll (preload) batch
        return contextManager.getVertex(reference);
    }

}
