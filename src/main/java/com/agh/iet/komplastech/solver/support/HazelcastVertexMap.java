package com.agh.iet.komplastech.solver.support;

import com.agh.iet.komplastech.solver.VertexId;
import com.agh.iet.komplastech.solver.productions.Production;
import com.hazelcast.core.IMap;
import com.hazelcast.map.EntryBackupProcessor;
import com.hazelcast.map.EntryProcessor;
import com.hazelcast.query.Predicates;

import java.util.List;
import java.util.Map;

public class HazelcastVertexMap implements VertexMap {

    private final IMap<VertexId, Vertex> vertexMap;

    public HazelcastVertexMap(IMap<VertexId, Vertex> vertexMap) {
        this.vertexMap = vertexMap;
    }

    @Override
    public void executeOnVertices(Production production, VertexRange range) {
        vertexMap.executeOnEntries(new ProductionAdapter(production),
                Predicates.between("id.id", range.start(), range.end()));

//        vertexMap.executeOnEntries(new ProductionAdapter(production),
//                (Predicate<VertexId, Vertex>) mapEntry -> mapEntry.getKey().isInRange(range));
    }

    @Override
    public List<Vertex> getSortedAtLevel(int level) {
        return null;
    }

    private class ProductionAdapter implements EntryProcessor<String, Vertex> {

        private final Production production;

        ProductionAdapter(Production production) {
            this.production = production;
        }

        @Override
        public Object process(Map.Entry<String, Vertex> entry) {
            return production.apply(entry.getValue());
        }

        @Override
        public EntryBackupProcessor<String, Vertex> getBackupProcessor() {
            return null;
        }

    }

}
