package com.agh.iet.komplastech.solver.storage;

import com.agh.iet.komplastech.solver.VertexId;
import com.agh.iet.komplastech.solver.support.Vertex;
import com.agh.iet.komplastech.solver.support.Vertex.VertexBuilder;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IdGenerator;

public class HazelcastObjectStore implements ObjectStore {

    private HazelcastInstance hazelcastInstance;

    public HazelcastObjectStore(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }

    @Override
    public Vertex createNewVertex(VertexBuilder vertexBuilder, VertexProcessor vertexProcessor) {
        IdGenerator vertexGenerator = hazelcastInstance.getIdGenerator("vertex");
        Vertex vertex = vertexBuilder.withId(VertexId.vertexId(vertexGenerator.newId())).build();
        vertexProcessor.onBeforeStore(vertex);
        storeVertex(vertex);
        return vertex;
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
