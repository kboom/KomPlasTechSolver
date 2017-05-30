package com.agh.iet.komplastech.solver.support;

import java.util.List;

public interface VertexMap {

    List<Vertex> getAllInRange(VertexRange vertexRange);

    List<Matrix> getUnknownsFor(VertexRange vertexRange);

}
