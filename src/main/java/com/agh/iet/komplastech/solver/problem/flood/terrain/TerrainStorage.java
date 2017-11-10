package com.agh.iet.komplastech.solver.problem.flood.terrain;

import java.util.stream.Stream;

public interface TerrainStorage {

    Stream<TerrainPoint> loadTerrainPoints();

    void saveTerrainPoints(Stream<TerrainPoint> terrainPointStream);

}
