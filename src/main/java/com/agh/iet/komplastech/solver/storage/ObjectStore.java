package com.agh.iet.komplastech.solver.storage;

import com.agh.iet.komplastech.solver.problem.Problem;
import com.agh.iet.komplastech.solver.support.ComputeConfig;
import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.support.Vertex;
import com.hazelcast.nio.serialization.DataSerializable;

public interface ObjectStore extends DataSerializable {

    void storeVertex(Vertex vertex);

    void clearAll();

    void clearVertices();

    void setProblem(Problem rhs);

    void setMesh(Mesh mesh);

    void setComputeConfig(ComputeConfig computeConfig);

}
