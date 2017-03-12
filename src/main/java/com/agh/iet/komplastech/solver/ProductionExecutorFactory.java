package com.agh.iet.komplastech.solver;

import com.agh.iet.komplastech.solver.productions.Production;
import com.agh.iet.komplastech.solver.support.HazelcastProductionAdapter;
import com.agh.iet.komplastech.solver.support.VertexRange;

import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

public class ProductionExecutorFactory {

    private final ExecutorService executorService;

    public ProductionExecutorFactory(ExecutorService executorService) {
        this.executorService = executorService;
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
                        range.getVerticesInRange().stream()
                                .map((vertexId) -> new HazelcastProductionAdapter(production, vertexId))
                                .collect(Collectors.toList())
                );
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        }

    }

}
