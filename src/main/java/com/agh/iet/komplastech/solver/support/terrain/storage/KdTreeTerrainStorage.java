package com.agh.iet.komplastech.solver.support.terrain.storage;

import com.agh.iet.komplastech.solver.support.terrain.TerrainPoint;
import com.agh.iet.komplastech.solver.support.terrain.TerrainPointFinder;
import com.agh.iet.komplastech.solver.support.terrain.support.KdTree2D;
import com.agh.iet.komplastech.solver.support.terrain.support.Point2D;

import java.util.stream.Stream;

public class KdTreeTerrainStorage implements TerrainStorage, TerrainPointFinder {

    private KdTree2D<Double> kdTree2D = new KdTree2D<>();

    @Override
    public Stream<TerrainPoint> loadTerrainPoints() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void saveTerrainPoints(Stream<TerrainPoint> terrainPointStream) {
        terrainPointStream.forEach(terrainPoint -> {
            kdTree2D.insert(new Point2D<>(terrainPoint.x, terrainPoint.y, terrainPoint.z));
        });
    }

    @Override
    public TerrainPoint get(double x, double y) {
        Point2D<Double> point = kdTree2D.nearest(new Point2D<>(x, y, 0d));
        return new TerrainPoint(point.x(), point.y(), point.getData());
    }

}