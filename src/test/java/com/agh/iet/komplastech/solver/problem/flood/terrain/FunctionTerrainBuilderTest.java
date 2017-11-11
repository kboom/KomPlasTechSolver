package com.agh.iet.komplastech.solver.problem.flood.terrain;

import com.agh.iet.komplastech.solver.support.Mesh;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

import static com.agh.iet.komplastech.solver.support.Mesh.aMesh;
import static org.assertj.core.api.Java6Assertions.assertThat;

public class FunctionTerrainBuilderTest {

    @Test
    public void canProduceFlatTerrain() {
        List<TerrainPoint> builtTerrain = FunctionTerrainBuilder.get()
                .withMesh(aMesh().withElementsX(2).withElementsY(2).build())
                .withFunction((x, y) -> 5d)
                .build()
                .collect(Collectors.toList());

        assertThat(builtTerrain).containsExactly(
                        new TerrainPoint(0,0, 5),
                        new TerrainPoint(0,1, 5),
                        new TerrainPoint(1,0, 5),
                        new TerrainPoint(1,1, 5)
                );
    }

    @Test
    public void canProduceInclinedTerrain() {
        List<TerrainPoint> builtTerrain = FunctionTerrainBuilder.get()
                .withMesh(aMesh().withElementsX(2).withElementsY(2).build())
                .withFunction((x, y) -> (double) x + y)
                .build()
                .collect(Collectors.toList());

        assertThat(builtTerrain).containsExactly(
                new TerrainPoint(0,0, 0),
                new TerrainPoint(0,1, 1),
                new TerrainPoint(1,0, 1),
                new TerrainPoint(1,1, 2)
        );
    }

}