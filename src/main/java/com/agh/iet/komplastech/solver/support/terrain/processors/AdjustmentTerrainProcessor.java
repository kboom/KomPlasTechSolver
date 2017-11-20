package com.agh.iet.komplastech.solver.support.terrain.processors;

import com.agh.iet.komplastech.solver.support.terrain.TerrainPoint;
import com.agh.iet.komplastech.solver.support.terrain.TerrainProcessor;
import com.agh.iet.komplastech.solver.support.terrain.support.Point2D;
import lombok.Builder;

import java.util.stream.Stream;

@Builder
public class AdjustmentTerrainProcessor implements TerrainProcessor {

    @Builder.Default private Point2D center = new Point2D(0,0);
    @Builder.Default private double scale = 1;

    @Override
    public Stream<TerrainPoint> analyze(Stream<TerrainPoint> terrainPointStream) {
        return terrainPointStream;
    }

    @Override
    public Stream<TerrainPoint> apply(Stream<TerrainPoint> terrainPointStream) {
        if(scale < 1) {
            return terrainPointStream.map(point -> new TerrainPoint(Math.round((center.x() + point.x) * scale), Math.round((center.y() + point.y) * scale), point.z));
        } else {
            return terrainPointStream.map(point -> new TerrainPoint(Math.round(center.x() + point.x * scale), Math.round(center.y() + point.y * scale), point.z));
        }
    }

    @Override
    public void teardown() {

    }

}
