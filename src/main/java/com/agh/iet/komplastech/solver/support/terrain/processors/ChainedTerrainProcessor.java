package com.agh.iet.komplastech.solver.support.terrain.processors;

import com.agh.iet.komplastech.solver.support.terrain.TerrainPoint;
import com.agh.iet.komplastech.solver.support.terrain.TerrainProcessor;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.stream.Stream;

import static com.agh.iet.komplastech.solver.support.terrain.processors.NoOpTerrainProcessor.NO_OP_TERRAIN_PROCESSOR;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ChainedTerrainProcessor implements TerrainProcessor {

    private TerrainProcessor delegate;
    private TerrainProcessor next;

    @Override
    public Stream<TerrainPoint> analyze(Stream<TerrainPoint> terrainPointStream) {
        return next.analyze(delegate.analyze(terrainPointStream));
    }

    @Override
    public Stream<TerrainPoint> apply(Stream<TerrainPoint> terrainPointStream) {
        return next.apply(delegate.apply(terrainPointStream));
    }

    @Override
    public void teardown() {
        delegate.teardown();
        next.teardown();
    }

    public static ChainedTerrainProcessor startingFrom(TerrainProcessor terrainProcessor) {
        return new ChainedTerrainProcessor(terrainProcessor, NO_OP_TERRAIN_PROCESSOR);
    }

    public ChainedTerrainProcessor withNext(TerrainProcessor terrainProcessor) {
        return new ChainedTerrainProcessor(this, terrainProcessor);
    }

}
