package com.agh.iet.komplastech.solver.terrain.processors;

import com.agh.iet.komplastech.solver.terrain.TerrainPoint;
import com.agh.iet.komplastech.solver.terrain.TerrainProcessor;
import com.agh.iet.komplastech.solver.terrain.support.Point2D;
import lombok.Builder;

import java.util.stream.Stream;

@Builder
public class AdjustmentTerrainProcessor implements TerrainProcessor {

    @Builder.Default private Point2D center = new Point2D(0,0);
    @Builder.Default private double scale = 1;

    @Override
    public void analyze(Stream<TerrainPoint> terrainPointStream) {

    }

    @Override
    public Stream<TerrainPoint> apply(Stream<TerrainPoint> terrainPointStream) {
        return terrainPointStream.map(point -> new TerrainPoint((point.x + center.x()) * scale, (point.y + center.y()) * scale, point.z));
    }

    @Override
    public void teardown() {

    }

}
