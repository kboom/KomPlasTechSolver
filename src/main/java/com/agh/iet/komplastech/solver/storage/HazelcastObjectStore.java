package com.agh.iet.komplastech.solver.storage;

import com.agh.iet.komplastech.solver.VertexId;
import com.agh.iet.komplastech.solver.support.HazelcastVertexMap;
import com.agh.iet.komplastech.solver.support.Vertex;
import com.agh.iet.komplastech.solver.support.VertexMap;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;

import java.io.IOException;

public class HazelcastObjectStore implements ObjectStore {

    private HazelcastInstance hazelcastInstance;

    public HazelcastObjectStore(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }

    @Override
    public void storeVertex(Vertex vertex) {
        getVertexMapInstance().put(vertex.getId(), vertex);
    }

    @Override
    public VertexMap getVertexMap() {
        return new HazelcastVertexMap(getVertexMapInstance());
    }

    @Override
    public void clearAll() {
        getVertexMapInstance().clear();
    }

    private IMap<VertexId, Vertex> getVertexMapInstance() {
        return hazelcastInstance.getMap("vertices");
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {

    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {

    }
}
