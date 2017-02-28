package com.agh.iet.komplastech.solver.storage;

import com.agh.iet.komplastech.solver.support.Vertex;
import com.hazelcast.core.HazelcastInstance;

public class HazelcastObjectStore implements ObjectStore {

    private HazelcastInstance hazelcastInstance;

    public HazelcastObjectStore(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }

    @Override
    public void storeVertex(Vertex vertex) {
        hazelcastInstance.getMap("vertices").put(vertex.getId(), vertex);
    }

    @Override
    public void getVertexById(long vertexId) {
        hazelcastInstance.getMap("vertices").get(vertexId);
    }

}
