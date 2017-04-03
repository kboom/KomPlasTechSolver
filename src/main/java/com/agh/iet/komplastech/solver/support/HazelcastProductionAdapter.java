package com.agh.iet.komplastech.solver.support;

import com.agh.iet.komplastech.solver.productions.Production;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.IMap;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;

import static com.agh.iet.komplastech.solver.factories.HazelcastGeneralFactory.GENERAL_FACTORY_ID;
import static com.agh.iet.komplastech.solver.factories.HazelcastGeneralFactory.PRODUCTION_ADAPTER;

public class HazelcastProductionAdapter
        implements HazelcastInstanceAware, Callable<Void>, IdentifiedDataSerializable {

    private transient HazelcastInstance hazelcastInstance;

    private Production production;

    private Set<VertexReference> verticesToApplyOn;

    private VertexRegionMapper vertexRegionMapper;

    @SuppressWarnings("unused")
    public HazelcastProductionAdapter() {

    }

    public HazelcastProductionAdapter(Production production,
                                      VertexRegionMapper vertexRegionMapper,
                                      Set<VertexReference> verticesToApplyOn) {
        this.production = production;
        this.verticesToApplyOn = verticesToApplyOn;
        this.vertexRegionMapper = vertexRegionMapper;
    }

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }

    @Override
    public Void call() {
        IMap<VertexReference, Vertex> vertices = hazelcastInstance.getMap("vertices");
        HazelcastProcessingContextManager contextManager = new HazelcastProcessingContextManager(hazelcastInstance, vertexRegionMapper);
        vertices.getAll(verticesToApplyOn).forEach((id, vertex) -> production.apply(contextManager.createFor(vertex)));
        contextManager.flush();
        return null;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeObject(production);
        out.writeObject(vertexRegionMapper);
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
        vertexRegionMapper = in.readObject();
        int vertexCount = in.readInt();
        verticesToApplyOn = new HashSet<>(vertexCount);
        for (int i = 0; i < vertexCount; i++) {
            verticesToApplyOn.add(in.readObject());
        }
    }

    @Override
    public int getFactoryId() {
        return GENERAL_FACTORY_ID;
    }

    @Override
    public int getId() {
        return PRODUCTION_ADAPTER;
    }

}