package com.agh.iet.komplastech.solver.support;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface ProcessingContextManager {

    void replaceVertices(Collection<Vertex> children);

    void replaceVertex(Vertex vertex);

    void storeVertex(Vertex vertex);

    Vertex getVertex(WeakVertexReference reference);

    void flush();

    <T> T getFromCache(String commonProcessingObjects);

    Map<VertexReference, Vertex> getAll(Set<VertexReference> verticesToApplyOn);

}
