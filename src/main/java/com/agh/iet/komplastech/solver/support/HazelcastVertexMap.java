package com.agh.iet.komplastech.solver.support;

import com.agh.iet.komplastech.solver.VertexId;
import com.hazelcast.core.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.stream.Collectors;

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

        Map<VertexId, Matrix> matricesByVertex = batchedStreamOf(regionsInRange.entrySet().stream(), 10)
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

    private static final class ExtractLeafMatricesForRegion
            implements Callable<Map<VertexId, Matrix>>, HazelcastInstanceAware, PartitionAware {

        private final RegionId regionId;
        private final VertexRange range;
        private HazelcastInstance hazelcastInstance;

        private ExtractLeafMatricesForRegion(RegionId regionId, VertexRange range) {
            this.regionId = regionId;
            this.range = range;
        }

        @Override
        public Map<VertexId, Matrix> call() throws Exception {
            IMap<VertexReference, Vertex> vertexMap = hazelcastInstance.getMap("vertices");

            Set<VertexReference> verticesInRange = range.getVerticesInRange().stream()
                    .map(id -> WeakVertexReference.weakReference(id, regionId)).collect(Collectors.toSet());

            return vertexMap
                    .getAll(verticesInRange)
                    .entrySet().stream()
                    .collect(Collectors.toMap(t -> t.getKey().getVertexId(), e -> e.getValue().m_x));
        }

        @Override
        public Object getPartitionKey() {
            return regionId.toInt();
        }

        @Override
        public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
            this.hazelcastInstance = hazelcastInstance;
        }

    }

}
