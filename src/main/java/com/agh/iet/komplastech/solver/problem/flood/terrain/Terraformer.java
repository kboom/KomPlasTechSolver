package com.agh.iet.komplastech.solver.problem.flood.terrain;

import com.agh.iet.komplastech.solver.support.Mesh;
import lombok.Builder;

import java.util.stream.IntStream;

@Builder
public final class Terraformer {

    private final TerrainStorage inputStorage;
    private final TerrainStorage outputStorage;
    private final TerrainProcessor terrainProcessor;

    public void terraform(Mesh mesh) {
        terrainProcessor.analyze(inputStorage.loadTerrainPoints()).count();
        outputStorage.saveTerrainPoints(terrainProcessor.apply(
                IntStream.range(0, mesh.getElementsX()).boxed().parallel().flatMap(
                    x -> IntStream.range(0, mesh.getElementsY()).mapToObj(y -> new TerrainPoint(x, y, 0))
                )
        ));
    }

}
