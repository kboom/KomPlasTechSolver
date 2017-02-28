package com.agh.iet.komplastech.solver.storage;

import com.agh.iet.komplastech.solver.support.Vertex;
import com.agh.iet.komplastech.solver.support.Vertex.VertexBuilder;

public interface ObjectStore {

    void storeVertex(Vertex vertex);

    void getVertexById(long vertexId);

}
