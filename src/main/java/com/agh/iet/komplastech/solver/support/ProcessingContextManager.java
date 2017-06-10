package com.agh.iet.komplastech.solver.support;

import java.util.Collection;

public interface ProcessingContextManager {

    void replaceVertices(Collection<Vertex> children);

    void replaceVertex(Vertex vertex);

    void storeVertex(Vertex vertex);

    Vertex getVertex(WeakVertexReference reference);

    void flush();

    <T> T getFromCache(CommonProcessingObject commonProcessingObjects);

}
