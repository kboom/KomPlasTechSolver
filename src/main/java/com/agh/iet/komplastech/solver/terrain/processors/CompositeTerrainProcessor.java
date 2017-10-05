package com.agh.iet.komplastech.solver.terrain.processors;

import com.agh.iet.komplastech.solver.terrain.TerrainPoint;
import com.agh.iet.komplastech.solver.terrain.TerrainProcessor;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Stream;

@AllArgsConstructor
public class CompositeTerrainProcessor implements TerrainProcessor {

    private List<TerrainProcessor> processingChain;

    @Override
    public void analyze(Stream<TerrainPoint> terrainPointStream) {
        processingChain.forEach(action -> action.analyze(terrainPointStream));
    }

    @Override
    public Stream<TerrainPoint> apply(Stream<TerrainPoint> terrainPointStream) {
        Stream<TerrainPoint> transformedStream = terrainPointStream;
        for(TerrainProcessor processor : processingChain) {
            transformedStream = processor.apply(terrainPointStream);
        }
        return transformedStream;
    }

    @Override
    public void teardown() {
        processingChain.forEach(TerrainProcessor::teardown);
    }
}
