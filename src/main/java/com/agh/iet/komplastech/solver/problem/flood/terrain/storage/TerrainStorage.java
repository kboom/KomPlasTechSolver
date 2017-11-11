package com.agh.iet.komplastech.solver.problem.flood.terrain.storage;

import com.agh.iet.komplastech.solver.problem.flood.terrain.TerrainPoint;

import java.util.stream.Stream;

public interface TerrainStorage {

    Stream<TerrainPoint> loadTerrainPoints();

    void saveTerrainPoints(Stream<TerrainPoint> terrainPointStream);

}
