package com.agh.iet.komplastech.solver.storage;

import com.agh.iet.komplastech.solver.problem.Problem;
import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.support.Vertex;
import com.agh.iet.komplastech.solver.support.VertexMap;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.Serializable;

public interface ObjectStore extends DataSerializable {

    void storeVertex(Vertex vertex);

    VertexMap getVertexMap();

    void clearAll();

    void clearVertices();

    void setProblem(Problem rhs);

    void setMesh(Mesh mesh);
}
