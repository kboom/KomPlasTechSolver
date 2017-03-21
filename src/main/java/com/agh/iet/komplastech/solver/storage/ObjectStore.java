package com.agh.iet.komplastech.solver.storage;

import com.agh.iet.komplastech.solver.support.Vertex;
import com.agh.iet.komplastech.solver.support.VertexMap;

import java.io.Serializable;

public interface ObjectStore extends Serializable {

    void storeVertex(Vertex vertex);

    VertexMap getVertexMap();

    void clearAll();
}
