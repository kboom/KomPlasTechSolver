package com.agh.iet.komplastech.solver.storage;

import com.agh.iet.komplastech.solver.Solution;
import com.agh.iet.komplastech.solver.problem.Problem;
import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.support.Vertex;
import com.agh.iet.komplastech.solver.support.VertexMap;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;

import java.io.IOException;

public class InMemoryObjectStore implements ObjectStore {
    @Override
    public void storeVertex(Vertex vertex) {

    }

    @Override
    public VertexMap getVertexMap() {
        return null;
    }

    @Override
    public void clearAll() {

    }

    @Override
    public void clearVertices() {

    }

    @Override
    public void setProblem(Problem rhs) {

    }

    @Override
    public void setMesh(Mesh mesh) {

    }

    @Override
    public void setSolution(Solution solution) {

    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {

    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {

    }
}
