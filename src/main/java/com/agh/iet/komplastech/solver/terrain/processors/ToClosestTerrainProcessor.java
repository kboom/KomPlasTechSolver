package com.agh.iet.komplastech.solver.terrain.processors;

import com.agh.iet.komplastech.solver.terrain.TerrainPoint;
import com.agh.iet.komplastech.solver.terrain.TerrainProcessor;
import com.agh.iet.komplastech.solver.terrain.support.KdTree2D;
import com.agh.iet.komplastech.solver.terrain.support.Point2D;

import java.util.stream.Stream;

public class ToClosestTerrainProcessor implements TerrainProcessor {

    private KdTree2D<Double> kdTree2D;

    @Override
    public void analyze(Stream<TerrainPoint> terrainPointStream) {
        kdTree2D = new KdTree2D<>();
        terrainPointStream.forEach(terrainPoint -> {
            kdTree2D.insert(toPoint(terrainPoint));
        });
    }

    @Override
    public Stream<TerrainPoint> apply(Stream<TerrainPoint> terrainPointStream) {
        return terrainPointStream.map(terrainPoint -> {
            Point2D<Double> nearestPoint = kdTree2D.nearest(toPoint(terrainPoint));
            return new TerrainPoint(nearestPoint.x(), nearestPoint.y(), nearestPoint.getData());
        });
    }

    @Override
    public void teardown() {
        kdTree2D = null;
    }

    private Point2D<Double> toPoint(TerrainPoint terrainPoint) {
        return new Point2D<>(terrainPoint.x, terrainPoint.y, terrainPoint.z);
    }

}
