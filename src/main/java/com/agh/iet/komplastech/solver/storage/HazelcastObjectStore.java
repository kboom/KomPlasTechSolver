package com.agh.iet.komplastech.solver.storage;

import com.agh.iet.komplastech.solver.support.*;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;

import java.io.IOException;

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
        getVertexMapInstance().clear();
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
