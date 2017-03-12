package com.agh.iet.komplastech.solver.storage;

import com.agh.iet.komplastech.solver.support.Vertex;
import com.agh.iet.komplastech.solver.support.VertexMap;

public interface ObjectStore {

    void storeVertex(Vertex vertex);

    VertexMap getVertexMap();

}
