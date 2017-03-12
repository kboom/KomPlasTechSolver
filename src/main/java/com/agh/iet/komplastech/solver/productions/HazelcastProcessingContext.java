package com.agh.iet.komplastech.solver.productions;

import com.agh.iet.komplastech.solver.support.Vertex;
import com.hazelcast.core.HazelcastInstance;

public class HazelcastProcessingContext implements ProcessingContext {

    private final Vertex vertex;
    private final HazelcastInstance hazelcastInstance;

    public HazelcastProcessingContext(HazelcastInstance hazelcastInstance, Vertex vertex) {
        this.vertex = vertex;
        this.hazelcastInstance = hazelcastInstance;
    }

    @Override
    public Vertex getVertex() {
        return vertex;
    }

    @Override
    public void storeVertex(Vertex vertex) {
        hazelcastInstance.getMap("vertices").put(vertex.getId(), vertex);
    }

}
