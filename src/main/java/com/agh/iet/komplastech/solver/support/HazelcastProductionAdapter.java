package com.agh.iet.komplastech.solver.support;

import com.agh.iet.komplastech.solver.VertexId;
import com.agh.iet.komplastech.solver.productions.Production;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.IMap;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class HazelcastProductionAdapter
        implements HazelcastInstanceAware, Callable<Void>, DataSerializable {

    private transient HazelcastInstance hazelcastInstance;

    private Production production;

    private Set<VertexId> verticesToApplyOn;

    @SuppressWarnings("unused")
    public HazelcastProductionAdapter() {

    }

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
        vertices.getAll(verticesToApplyOn).forEach((id, vertex) -> production.apply(contextManager.createFor(vertex)));
        contextManager.flush();
        return null;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeObject(production);
        out.writeInt(verticesToApplyOn.size());
        verticesToApplyOn.forEach(vertex -> {
            try {
                out.writeObject(vertex);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        });
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        production = in.readObject();
        int vertexCount = in.readInt();
        verticesToApplyOn = new HashSet<>(vertexCount);
        for(int i = 0; i < vertexCount; i++) {
            verticesToApplyOn.add(in.readObject());
        }
    }

}