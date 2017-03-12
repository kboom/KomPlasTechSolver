package com.agh.iet.komplastech.solver.support;

import com.agh.iet.komplastech.solver.VertexId;

public interface ReferenceVisitor {

    Vertex loadVertex(VertexId vertexId);

}
