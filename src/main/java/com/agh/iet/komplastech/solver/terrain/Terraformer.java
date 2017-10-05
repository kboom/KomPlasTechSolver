package com.agh.iet.komplastech.solver.terrain;

import lombok.Builder;

import java.util.stream.Stream;

@Builder
public final class Terraformer {

    private final TerrainStorage inputStorage;
    private final TerrainStorage outputStorage;
    private final TerrainProcessor terrainProcessor;

    public void terraform() {
        Stream<TerrainPoint> terrainPointStream = inputStorage.loadTerrainPoints();
        terrainProcessor.analyze(terrainPointStream);
        outputStorage.saveTerrainPoints(terrainProcessor.apply(inputStorage.loadTerrainPoints()));
    }

}
