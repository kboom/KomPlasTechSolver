package com.agh.iet.komplastech.solver.problem.flood.terrain.processors;

import com.agh.iet.komplastech.solver.problem.flood.terrain.TerrainPoint;
import org.junit.Test;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class ToClosestTerrainProcessorTest {

    private static final ToClosestTerrainProcessor processor = new ToClosestTerrainProcessor();

    @Test
    public void mapsPointsToClosestOnes() {
        Stream<TerrainPoint> terrainPointStream = Stream.of(
                new TerrainPoint(0, 0, 0), new TerrainPoint(1, 1, 1),
                new TerrainPoint(3, 3, 3), new TerrainPoint(3, 4, 55)
        );

        processor.analyze(terrainPointStream);

        terrainPointStream.count();

        Stream<TerrainPoint> resultStream = processor.apply(Stream.of(
                new TerrainPoint(3, 1, 0),
                new TerrainPoint(4, 2, 0))
        );

        assertThat(resultStream.collect(Collectors.toList()))
                .containsExactlyInAnyOrder(
                        new TerrainPoint(1, 1, 1),
                        new TerrainPoint(3, 3, 3)
                );
    }

}