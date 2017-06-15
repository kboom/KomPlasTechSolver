package com.agh.iet.komplastech.solver.storage;

import com.agh.iet.komplastech.solver.problem.Problem;
import com.agh.iet.komplastech.solver.support.*;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;

import java.io.IOException;

import static com.agh.iet.komplastech.solver.support.CommonProcessingObject.*;

public class HazelcastObjectStore implements ObjectStore {

    private final IMap<VertexReference, Vertex> vertexMap;
    private final IMap<CommonProcessingObject, Object> commonsMap;

    public HazelcastObjectStore(HazelcastInstance hazelcastInstance) {
        vertexMap = hazelcastInstance.getMap("vertices");
        commonsMap = hazelcastInstance.getMap("commons");
    }

    @Override
    public void storeVertex(Vertex vertex) {
        vertexMap.put(vertex.getVertexReference(), vertex);
    }

    @Override
    public void clearAll() {
        clearVertices();
        commonsMap.clear();
    }

    @Override
    public void clearVertices() {
        vertexMap.clear();
    }

    @Override
    public void setProblem(Problem rhs) {
        commonsMap.put(PROBLEM, rhs);
    }

    @Override
    public void setMesh(Mesh mesh) {
        commonsMap.put(MESH, mesh);
    }

    @Override
    public void setComputeConfig(ComputeConfig computeConfig) {
        commonsMap.put(COMPUTE_CONFIG, computeConfig);
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {

    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {

    }
}
