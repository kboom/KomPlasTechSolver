package com.agh.iet.komplastech.solver.support;

import com.agh.iet.komplastech.solver.VertexId;

import java.util.Collection;

public interface ProcessingContextManager {

    void replaceVertices(Collection<Vertex> children);

    void replaceVertex(Vertex vertex);

    void storeVertex(Vertex vertex);

    Vertex getVertex(VertexId vertexId);

    void flush();

    <T> T getFromCache(CommonProcessingObject commonProcessingObjects);

}
