package com.agh.iet.komplastech.solver.terrain;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FileTerrainStorageTest {

    @Test
    public void canReadTerrainAsStream() {
        FileTerrainStorage terrainStorage = FileTerrainStorage.builder()
                .inFilePath(getClass().getClassLoader().getResource("terrain.txt").getFile()).build();

        assertThat(terrainStorage.loadTerrainPoints()).containsExactly(
                new TerrainPoint(568900.00, 295200.00, 272.66),
                new TerrainPoint(569000.00, 295200.00, 272.59),
                new TerrainPoint(449100.00, 333200.00, 111.71)
        );
    }

}