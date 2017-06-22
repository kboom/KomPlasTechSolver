package com.agh.iet.komplastech.solver.support;

import com.agh.iet.komplastech.solver.factories.HazelcastGeneralFactory.GeneralObjectType;
import com.agh.iet.komplastech.solver.productions.ProcessingContext;
import com.agh.iet.komplastech.solver.productions.Production;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.PartitionAware;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static com.agh.iet.komplastech.solver.factories.HazelcastGeneralFactory.GENERAL_FACTORY_ID;

// http://docs.hazelcast.org/docs/2.0/manual/html-single/#DataAffinity
public class HazelcastProductionAdapter
        implements HazelcastInstanceAware, Callable<Void>, PartitionAware, IdentifiedDataSerializable {

    private transient HazelcastInstance hazelcastInstance;

    private static final ExecutorService executorService = Executors.newCachedThreadPool();

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
        Map<VertexReference, Vertex> vertexMap = contextManager.getAll(verticesToApplyOn);
        if(vertexMap.size() > 1) {
            executeInParallel(contextManager, vertexMap);
        } else {
            executeSynchronously(contextManager, vertexMap);
        }
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

    private static class ContextTask implements Callable<Void> {

        private ProcessingContext context;
        private Production production;

        ContextTask(ProcessingContext context, Production production) {
            this.context = context;
            this.production = production;
        }

        @Override
        public Void call() throws Exception {
            production.apply(context);
            return null;
        }

    }

    private void executeSynchronously(HazelcastProcessingContextManager contextManager, Map<VertexReference, Vertex> vertexMap) {
        vertexMap.forEach((id, vertex) -> production.apply(contextManager.createFor(vertex)));
    }

    private void executeInParallel(HazelcastProcessingContextManager contextManager, Map<VertexReference, Vertex> vertexMap) {
        try {
            executorService.invokeAll(
                    vertexMap
                            .values().stream().map(contextManager::createFor)
                            .map(context -> new ContextTask(context, production)).collect(Collectors.toList())
            ).forEach(future -> {
                try {
                    future.get();
                } catch (Exception e) {
                    throw new IllegalStateException("Could not get results for production", e);
                }
            });
        } catch (InterruptedException e) {
            throw new IllegalStateException("Could not launch production", e);
        }
    }

    @Override
    public int getFactoryId() {
        return GENERAL_FACTORY_ID;
    }

    @Override
    public int getId() {
        return GeneralObjectType.PRODUCTION_ADAPTER.id;
    }

    @Override
    public Object getPartitionKey() {
        return regionId.toInt();
    }

}