package com.agh.iet.komplastech.solver;

import com.agh.iet.komplastech.solver.productions.Production;
import com.agh.iet.komplastech.solver.support.VertexMap;
import com.agh.iet.komplastech.solver.support.VertexRange;

public class ProductionExecutorFactory {

    private final VertexMap vertexMap;

    public ProductionExecutorFactory(VertexMap vertexMap) {
        this.vertexMap = vertexMap;
    }

    public ProductionLauncher launchProduction(Production production) {
        return new ProductionLauncher(production);
    }

    public class ProductionLauncher {

        private final Production production;
        private VertexRange range;

        private ProductionLauncher(Production production) {
            this.production = production;
        }

        public ProductionLauncher inVertexRange(VertexRange range) {
            this.range = range;
            return this;
        }

        public void andWaitTillComplete() {
            vertexMap.executeOnVertices(production, range);
        }

    }

}
