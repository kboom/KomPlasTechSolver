package com.agh.iet.komplastech.solver.terrain;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class MapTerrainStorage implements TerrainStorage, TerrainPointFinder {

    private Map<String, TerrainPoint> terrainPointMap = new ConcurrentHashMap<>();

    @Override
    public Stream<TerrainPoint> loadTerrainPoints() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void saveTerrainPoints(Stream<TerrainPoint> terrainPointStream) {
        terrainPointStream.forEach(point -> terrainPointMap.put(String.format("%d,%d", (int) Math.round(point.x), (int) Math.round(point.y)), point));
    }

    @Override
    public TerrainPoint get(double x, double y) {
        return terrainPointMap.get(String.format("%d,%d", (int) Math.round(x), (int) Math.round(y)));
    }

}
