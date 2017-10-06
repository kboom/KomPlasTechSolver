package com.agh.iet.komplastech.solver.terrain;

import com.agh.iet.komplastech.solver.support.Mesh;
import lombok.Builder;

import java.util.stream.IntStream;

@Builder
public final class Terraformer {

    private final TerrainStorage inputStorage;
    private final TerrainStorage outputStorage;
    private final TerrainProcessor terrainProcessor;

    public void terraform(Mesh mesh) {
        terrainProcessor.analyze(inputStorage.loadTerrainPoints()).forEach(t -> System.out.println("Processed " + t));
        outputStorage.saveTerrainPoints(terrainProcessor.apply(
                IntStream.range(0, mesh.getElementsX()).boxed().flatMap(
                    x -> IntStream.range(0, mesh.getElementsY()).mapToObj(y -> new TerrainPoint(x, y, 0))
                )
        ));
    }

}
