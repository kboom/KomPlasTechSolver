package com.agh.iet.komplastech.solver.terrain;

import java.util.stream.Stream;

public interface TerrainProcessor {

    Stream<TerrainPoint> analyze(Stream<TerrainPoint> terrainPointStream);

    Stream<TerrainPoint> apply(Stream<TerrainPoint> terrainPointStream);

    void teardown();

}
