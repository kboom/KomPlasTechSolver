package com.agh.iet.komplastech.solver.terrain.processors;

import com.agh.iet.komplastech.solver.terrain.TerrainPoint;
import com.agh.iet.komplastech.solver.terrain.TerrainProcessor;
import com.agh.iet.komplastech.solver.terrain.support.KDTree;

import java.util.stream.Stream;

public class RegularMeshTerrainProcessor implements TerrainProcessor {

    private KDTree kdTree;

    @Override
    public void analyze(Stream<TerrainPoint> terrainPointStream) {
        kdTree = new KDTree((int) terrainPointStream.count());
        terrainPointStream.forEach(terrainPoint -> {
            kdTree.add(toListOfCoordinates(terrainPoint));
        });
    }

    @Override
    public Stream<TerrainPoint> apply(Stream<TerrainPoint> terrainPointStream) {
        return terrainPointStream.map(terrainPoint -> {
            double [] nearestCoordinates = kdTree.findNearest(toListOfCoordinates(terrainPoint));
            return TerrainPoint.builder().x(nearestCoordinates[0]).y(nearestCoordinates[1]).z(terrainPoint.y).build();
        });
    }

    @Override
    public void teardown() {

    }

    private double[] toListOfCoordinates(TerrainPoint terrainPoint) {
        return new double[] { terrainPoint.x, terrainPoint.y };
    }

}
