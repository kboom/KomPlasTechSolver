package com.agh.iet.komplastech.solver.storage;

import com.agh.iet.komplastech.solver.Solution;
import com.agh.iet.komplastech.solver.problem.Problem;
import com.agh.iet.komplastech.solver.support.*;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;

import java.io.IOException;

import static com.agh.iet.komplastech.solver.support.CommonProcessingObject.MESH;
import static com.agh.iet.komplastech.solver.support.CommonProcessingObject.PROBLEM;
import static com.agh.iet.komplastech.solver.support.CommonProcessingObject.SOLUTION;

public class HazelcastObjectStore implements ObjectStore {

    private HazelcastInstance hazelcastInstance;
    private VertexRegionMapper vertexRegionMapper;

    public HazelcastObjectStore(HazelcastInstance hazelcastInstance, VertexRegionMapper vertexRegionMapper) {
        this.hazelcastInstance = hazelcastInstance;
        this.vertexRegionMapper = vertexRegionMapper;
    }

    @Override
    public void storeVertex(Vertex vertex) {
        getVertexMapInstance().put(vertex.getVertexReference(), vertex);
    }

    @Override
    public VertexMap getVertexMap() {
        return new HazelcastVertexMap(getVertexMapInstance(), vertexRegionMapper);
    }

    @Override
    public void clearAll() {
        clearVertices();
        hazelcastInstance.getMap("commons").clear();
    }

    @Override
    public void clearVertices() {
        getVertexMapInstance().clear();
    }

    @Override
    public void setProblem(Problem rhs) {
        hazelcastInstance.getMap("commons").put(PROBLEM, rhs);
    }

    @Override
    public void setMesh(Mesh mesh) {
        hazelcastInstance.getMap("commons").put(MESH, mesh);
    }

    @Override
    public void setSolution(Solution solution) {
        hazelcastInstance.getMap("commons").put(SOLUTION, solution);
    }

    private IMap<VertexReference, Vertex> getVertexMapInstance() {
        return hazelcastInstance.getMap("vertices");
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {

    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {

    }
}
