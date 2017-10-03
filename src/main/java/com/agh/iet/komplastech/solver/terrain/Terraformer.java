package com.agh.iet.komplastech.solver.terrain;

import lombok.Builder;

@Builder
public final class Terraformer {

    private final TerrainStorage inputStorage;
    private final TerrainStorage outputStorage;
    private final TerrainProcessor terrainProcessor;

    public void terraform() {
        outputStorage.saveTerrainPoints(
                terrainProcessor.apply(inputStorage.loadTerrainPoints())
        );
    }

}
