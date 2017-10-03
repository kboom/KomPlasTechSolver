package com.agh.iet.komplastech.solver.terrain;

import java.util.stream.Stream;

public interface TerrainProcessor {

    void analyze(Stream<TerrainPoint> terrainPointStream);

    Stream<TerrainPoint> apply(Stream<TerrainPoint> terrainPointStream);

    void teardown();

}
