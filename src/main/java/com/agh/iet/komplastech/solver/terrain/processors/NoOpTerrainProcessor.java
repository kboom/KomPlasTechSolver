package com.agh.iet.komplastech.solver.terrain.processors;

import com.agh.iet.komplastech.solver.terrain.TerrainPoint;
import com.agh.iet.komplastech.solver.terrain.TerrainProcessor;

import java.util.stream.Stream;

public class NoOpTerrainProcessor implements TerrainProcessor {

    public static final NoOpTerrainProcessor NO_OP_TERRAIN_PROCESSOR = new NoOpTerrainProcessor();

    @Override
    public Stream<TerrainPoint> analyze(Stream<TerrainPoint> terrainPointStream) {
        return terrainPointStream;
    }

    @Override
    public Stream<TerrainPoint> apply(Stream<TerrainPoint> terrainPointStream) {
        return terrainPointStream;
    }

    @Override
    public void teardown() {

    }

}
