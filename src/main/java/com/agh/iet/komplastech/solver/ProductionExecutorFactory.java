package com.agh.iet.komplastech.solver;

import com.agh.iet.komplastech.solver.VertexId;
import com.agh.iet.komplastech.solver.productions.Production;
import com.agh.iet.komplastech.solver.support.VertexMap;

import java.util.Set;

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
        private Set<VertexId> vertexIds;

        private ProductionLauncher(Production production) {
            this.production = production;
        }

        public ProductionLauncher onVertices(Set<VertexId> vertices) {
            this.vertexIds = vertices;
            return this;
        }

        public void andWaitTillComplete() {
            vertexMap.executeOnVertices(vertexIds, production);
        }

    }

}
