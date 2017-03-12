package com.agh.iet.komplastech.solver.storage;

import com.agh.iet.komplastech.solver.VertexId;
import com.agh.iet.komplastech.solver.support.HazelcastVertexMap;
import com.agh.iet.komplastech.solver.support.Vertex;
import com.agh.iet.komplastech.solver.support.VertexMap;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

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

    private IMap<VertexId, Vertex> getVertexMapInstance() {
        return hazelcastInstance.getMap("vertices");
    }

}
