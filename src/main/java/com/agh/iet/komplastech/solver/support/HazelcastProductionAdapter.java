package com.agh.iet.komplastech.solver.support;

import com.agh.iet.komplastech.solver.productions.Production;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.PartitionAware;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;

import static com.agh.iet.komplastech.solver.factories.HazelcastGeneralFactory.GENERAL_FACTORY_ID;
import static com.agh.iet.komplastech.solver.factories.HazelcastGeneralFactory.PRODUCTION_ADAPTER;

// http://docs.hazelcast.org/docs/2.0/manual/html-single/#DataAffinity
public class HazelcastProductionAdapter
        implements HazelcastInstanceAware, Callable<Void>, PartitionAware, IdentifiedDataSerializable {

    private transient HazelcastInstance hazelcastInstance;

    private Production production;

    private RegionId regionId;

    private Set<VertexReference> verticesToApplyOn;

    @SuppressWarnings("unused")
    public HazelcastProductionAdapter() {

    }

    public HazelcastProductionAdapter(RegionId regionId,
                                      Production production,
                                      Set<VertexReference> verticesToApplyOn) {
        this.regionId = regionId;
        this.production = production;
        this.verticesToApplyOn = verticesToApplyOn;
    }

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }

    @Override
    public Void call() {
        HazelcastProcessingContextManager contextManager = new HazelcastProcessingContextManager(hazelcastInstance);
        contextManager.getAll(verticesToApplyOn).forEach((id, vertex) -> production.apply(contextManager.createFor(vertex)));
        contextManager.flush();
        return null;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeObject(regionId);
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
        regionId = in.readObject();
        production = in.readObject();
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

    @Override
    public Object getPartitionKey() {
        return regionId.toInt();
    }

}