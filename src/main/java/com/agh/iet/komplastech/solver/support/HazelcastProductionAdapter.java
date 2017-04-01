package com.agh.iet.komplastech.solver.support;

import com.agh.iet.komplastech.solver.VertexId;
import com.agh.iet.komplastech.solver.productions.Production;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.IMap;

import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.Callable;

public class HazelcastProductionAdapter
        implements HazelcastInstanceAware, Callable<Void>, Serializable {

    private transient HazelcastInstance hazelcastInstance;

    private Production production;

    private Set<VertexId> verticesToApplyOn;

    public HazelcastProductionAdapter(Production production, Set<VertexId> verticesToApplyOn) {
        this.production = production;
        this.verticesToApplyOn = verticesToApplyOn;
    }

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }

    @Override
    public Void call() {
        IMap<VertexId, Vertex> vertices = hazelcastInstance.getMap("vertices");
        HazelcastProcessingContextManager contextManager = new HazelcastProcessingContextManager(hazelcastInstance);
        vertices.loadAll(verticesToApplyOn, false);
        vertices.getAll(verticesToApplyOn).forEach((id, vertex) -> production.apply(contextManager.createFor(vertex)));
        contextManager.flush();
        return null;
    }

}