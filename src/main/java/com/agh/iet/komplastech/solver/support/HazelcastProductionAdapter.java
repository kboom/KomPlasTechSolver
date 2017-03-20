package com.agh.iet.komplastech.solver.support;

import com.agh.iet.komplastech.solver.VertexId;
import com.agh.iet.komplastech.solver.productions.HazelcastProcessingContext;
import com.agh.iet.komplastech.solver.productions.Production;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.IMap;

import java.io.Serializable;
import java.util.concurrent.Callable;

public class HazelcastProductionAdapter
        implements HazelcastInstanceAware, Callable<Vertex>, Serializable {

    private transient HazelcastInstance hazelcastInstance;

    private Production production;

    /**
     * This might be a list... then could fetch a batches...
     */
    private VertexId vertexId;

    public HazelcastProductionAdapter(Production production, VertexId vertexId) {
        this.production = production;
        this.vertexId = vertexId;
    }

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }

    @Override
    public Vertex call() {
        IMap<VertexId, Vertex> vertices = hazelcastInstance.getMap("vertices");
        Vertex vertex = vertices.get(vertexId);
        Vertex modifiedVertex = production.apply(new HazelcastProcessingContext(hazelcastInstance, vertex));
        vertices.replace(vertexId, modifiedVertex);
        return modifiedVertex;
    }

}