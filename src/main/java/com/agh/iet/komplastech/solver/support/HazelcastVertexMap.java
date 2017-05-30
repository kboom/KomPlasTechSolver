package com.agh.iet.komplastech.solver.support;

import com.agh.iet.komplastech.solver.VertexId;
import com.hazelcast.core.IMap;
import com.hazelcast.map.EntryBackupProcessor;
import com.hazelcast.map.EntryProcessor;
import com.hazelcast.query.Predicate;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.agh.iet.komplastech.solver.support.VertexReferenceFunctionAdapter.toVertexReferenceUsing;

public class HazelcastVertexMap implements VertexMap {

    private final IMap<VertexReference, Vertex> vertexMap;

    private final VertexRegionMapper vertexRegionMapper;

    public HazelcastVertexMap(IMap<VertexReference, Vertex> vertexMap, VertexRegionMapper vertexRegionMapper) {
        this.vertexMap = vertexMap;
        this.vertexRegionMapper = vertexRegionMapper;
    }

    @Override
    public List<Vertex> getAllInRange(VertexRange vertexRange) {
        return vertexMap.getAll(getVertexReferencesFor(vertexRange))
                .values()
                .stream()
                .sorted((v1, v2) ->
                        v1.getVertexId().getAbsoluteIndex() - v2.getVertexId().getAbsoluteIndex()
                ).collect(Collectors.toList());
    }

    @Override
    public List<Matrix> getUnknownsFor(VertexRange vertexRange) {
        Map<VertexReference, Object> matrix = vertexMap.executeOnEntries(new GetUnknownsVertexProcessor(),
                new InVertexRangePredicate(vertexRange));

        return matrix.entrySet().parallelStream().sorted((v1, v2) -> {
            final VertexId v1key = v1.getKey().getVertexId();
            final VertexId v2key = v2.getKey().getVertexId();
            return v1key.getAbsoluteIndex() - v2key.getAbsoluteIndex();
        }).collect(Collectors.mapping((e) -> (Matrix) e.getValue(), Collectors.toList()));
    }

    private Set<VertexReference> getVertexReferencesFor(VertexRange vertexRange) {
        return vertexRange.getVerticesInRange()
                .stream()
                .map(toVertexReferenceUsing(vertexRegionMapper))
                .collect(Collectors.toSet());
    }

    private static final class InVertexRangePredicate implements Predicate<VertexReference, Vertex> {

        private final VertexRange vertexRange;

        InVertexRangePredicate(VertexRange vertexRange) {
            this.vertexRange = vertexRange;
        }

        @Override
        public boolean apply(Map.Entry<VertexReference, Vertex> mapEntry) {
            final VertexId vertexId = mapEntry.getKey().getVertexId();
            return vertexRange.includes(vertexId);
        }

    }

    private static final class GetUnknownsVertexProcessor implements EntryProcessor<VertexReference, Vertex> {

        @Override
        public Object process(Map.Entry<VertexReference, Vertex> entry) {
            return entry.getValue().m_x;
        }

        @Override
        public EntryBackupProcessor<VertexReference, Vertex> getBackupProcessor() {
            return null;
        }

    }


}
