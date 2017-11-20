package com.agh.iet.komplastech.solver.support.terrain.storage;

import com.agh.iet.komplastech.solver.support.terrain.TerrainPoint;

import java.util.stream.Stream;

public interface TerrainStorage {

    Stream<TerrainPoint> loadTerrainPoints();

    void saveTerrainPoints(Stream<TerrainPoint> terrainPointStream);

}
