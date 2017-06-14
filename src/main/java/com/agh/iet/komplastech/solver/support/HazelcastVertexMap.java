package com.agh.iet.komplastech.solver.support;

import com.agh.iet.komplastech.solver.VertexId;
import com.agh.iet.komplastech.solver.factories.HazelcastGeneralFactory;
import com.hazelcast.core.*;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.agh.iet.komplastech.solver.factories.HazelcastGeneralFactory.GENERAL_FACTORY_ID;
import static com.agh.iet.komplastech.solver.factories.HazelcastGeneralFactory.MATRIX_EXTRACTOR;
import static com.agh.iet.komplastech.solver.support.BatchingIterator.batchedStreamOf;
import static com.agh.iet.komplastech.solver.support.VertexReferenceFunctionAdapter.toVertexReferenceUsing;
import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.toMap;

public class HazelcastVertexMap implements VertexMap {

    private final IMap<VertexReference, Vertex> vertexMap;

    private final VertexRegionMapper vertexRegionMapper;

    private final IExecutorService executorService;

    public HazelcastVertexMap(IExecutorService executorService,
                              IMap<VertexReference, Vertex> vertexMap,
                              VertexRegionMapper vertexRegionMapper) {
        this.executorService = executorService;
        this.vertexMap = vertexMap;
        this.vertexRegionMapper = vertexRegionMapper;
    }

    @Override
    public List<Vertex> getAllInRange(VertexRange vertexRange) {
        return vertexMap.getAll(getVertexReferencesFor(vertexRange))
                .values()
                .stream()
                .sorted(comparingInt(v -> v.getVertexId().getAbsoluteIndex())
                ).collect(Collectors.toList());
    }

    @Override
    public List<Matrix> getUnknownsFor(VertexRange vertexRange) {
        final Map<RegionId, VertexRange> regionsInRange = vertexRegionMapper.getRegionsInRange(vertexRange);

        Map<VertexId, Matrix> matricesByVertex = batchedStreamOf(regionsInRange.entrySet().stream(), 10000)
                .map(regionSet -> regionSet.parallelStream()
                        .map(e -> new ExtractLeafMatricesForRegion(e.getKey(), e.getValue()))
                        .map(executorService::submit)
                        .map(future -> {
                            try {
                                return future.get();
                            } catch (Exception e) {
                                throw new IllegalStateException(e);
                            }
                        })).flatMap(Function.identity())
                .map(MatricesByVertex::get)
                .map(Map::entrySet)
                .map(Set::stream)
                .flatMap(Function.identity())
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));

        return matricesByVertex.entrySet().parallelStream().sorted((v1, v2) -> {
            final VertexId v1key = v1.getKey();
            final VertexId v2key = v2.getKey();
            return v1key.getAbsoluteIndex() - v2key.getAbsoluteIndex();
        }).map(Map.Entry::getValue).collect(Collectors.toList());
    }

    private Set<VertexReference> getVertexReferencesFor(VertexRange vertexRange) {
        return vertexRange.getVerticesInRange()
                .stream()
                .map(toVertexReferenceUsing(vertexRegionMapper))
                .collect(Collectors.toSet());
    }

    public static final class ExtractLeafMatricesForRegion
            implements Callable<MatricesByVertex>, HazelcastInstanceAware, PartitionAware, IdentifiedDataSerializable {

        private RegionId regionId;
        private VertexRange range;
        private HazelcastInstance hazelcastInstance;

        private ExtractLeafMatricesForRegion(RegionId regionId, VertexRange range) {
            this.regionId = regionId;
            this.range = range;
        }

        @SuppressWarnings("unused")
        public ExtractLeafMatricesForRegion() {}

        @Override
        public MatricesByVertex call() throws Exception {
            IMap<VertexReference, Vertex> vertexMap = hazelcastInstance.getMap("vertices");

            Set<VertexReference> verticesInRange = range.getVerticesInRange().stream()
                    .map(id -> WeakVertexReference.weakReference(id, regionId)).collect(Collectors.toSet());

            Map<VertexId, Matrix> matricesByVertex = vertexMap
                    .getAll(verticesInRange)
                    .entrySet().stream()
                    .collect(Collectors.toMap(t -> t.getKey().getVertexId(), e -> e.getValue().m_x));

            return new MatricesByVertex(matricesByVertex);
        }

        @Override
        public Object getPartitionKey() {
            return regionId.toInt();
        }

        @Override
        public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
            this.hazelcastInstance = hazelcastInstance;
        }

        @Override
        public int getFactoryId() {
            return GENERAL_FACTORY_ID;
        }

        @Override
        public int getId() {
            return MATRIX_EXTRACTOR;
        }

        @Override
        public void writeData(ObjectDataOutput out) throws IOException {
            out.writeObject(regionId);
            out.writeObject(range);
        }

        @Override
        public void readData(ObjectDataInput in) throws IOException {
            regionId = in.readObject();
            range = in.readObject();
        }

    }

}
