package com.agh.iet.komplastech.solver;

import com.agh.iet.komplastech.solver.productions.Production;
import com.agh.iet.komplastech.solver.support.HazelcastProductionAdapter;
import com.agh.iet.komplastech.solver.support.VertexRange;

import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

import static com.agh.iet.komplastech.solver.support.BatchingIterator.batchedStreamOf;

public class ProductionExecutorFactory {

    private final ExecutorService executorService;

    private final int maxBatchSize;

    public ProductionExecutorFactory(ExecutorService executorService, int maxBatchSize) {
        this.executorService = executorService;
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

        public void andWaitTillComplete() {
            try {
                executorService.invokeAll(
                        batchedStreamOf(range.getVerticesInRange().stream(), maxBatchSize)
                                .map((vertexBatch) -> new HazelcastProductionAdapter(production, vertexBatch))
                                .collect(Collectors.toList())
                );
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        }

    }

}
