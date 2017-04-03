package com.agh.iet.komplastech.solver;

import com.agh.iet.komplastech.solver.productions.Production;
import com.agh.iet.komplastech.solver.support.*;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IExecutorService;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.agh.iet.komplastech.solver.support.BatchingIterator.batchedStreamOf;
import static com.agh.iet.komplastech.solver.support.VertexReferenceFunctionAdapter.toVertexReferenceUsing;

public class ProductionExecutorFactory {

    private final IExecutorService executorService;

    private final int maxBatchSize;

    private final VertexRegionMapper regionMapper;

    ProductionExecutorFactory(HazelcastInstance hazelcastInstance, VertexRegionMapper regionMapper, int maxBatchSize) {
        this.executorService = hazelcastInstance.getExecutorService("production");
        this.regionMapper = regionMapper;
        this.maxBatchSize = maxBatchSize;
    }

    public ProductionLauncher launchProduction(Production production) {
        return new ProductionLauncher(production);
    }

    public class ProductionLauncher {

        private Production production;
        private VertexRange range;

        private ProductionLauncher(Production production) {
            this.production = production;
        }

        public ProductionLauncher inVertexRange(VertexRange range) {
            this.range = range;
            return this;
        }

        /**
         *
         */
        public void andWaitTillComplete() {
            Stream<VertexReference> vertexReferenceStream = mapToReferences(range);
            Map<RegionId, Set<VertexReference>> referencesByRegion = groupByRegion(vertexReferenceStream);

            Stream<Future<Void>> futureStream = referencesByRegion.entrySet().stream().map((entry) -> {
                final RegionId region = entry.getKey();
                final Set<VertexReference> vertices = entry.getValue();

                return batchedStreamOf(vertices.stream(), maxBatchSize)
                        .map((vertexBatch) -> new HazelcastProductionAdapter(production, regionMapper, vertexBatch))
                        .map(production -> executorService.submitToKeyOwner(production, region));
            }).flatMap(Function.identity());

            waitForCompletion(futureStream);
        }

        private Stream<VertexReference> mapToReferences(VertexRange vertexRange) {
            return vertexRange.getVerticesInRange().stream().map(toVertexReferenceUsing(regionMapper));
        }

        private Map<RegionId, Set<VertexReference>> groupByRegion(Stream<VertexReference> vertexReferenceStream) {
            return vertexReferenceStream
                    .collect(Collectors.groupingBy(VertexReference::getRegionId,
                            Collectors.mapping(Function.identity(), Collectors.toSet())));
        }

        private void waitForCompletion(Stream<Future<Void>> futureStream) {
            futureStream.forEach(future -> {
                try {
                    future.get();
                } catch (Exception e) {
                    throw new IllegalStateException(e);
                }
            });
        }

    }

}
