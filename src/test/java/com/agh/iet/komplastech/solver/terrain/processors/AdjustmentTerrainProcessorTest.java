package com.agh.iet.komplastech.solver.terrain.processors;

import com.agh.iet.komplastech.solver.terrain.TerrainPoint;
import com.agh.iet.komplastech.solver.terrain.support.Point2D;
import org.junit.Before;
import org.junit.Test;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class AdjustmentTerrainProcessorTest {

    @Test
    public void addsOffset() {
        final AdjustmentTerrainProcessor adjustmentTerrainProcessor = AdjustmentTerrainProcessor
                .builder().center(new Point2D(1, 1)).build();

        assertThat(adjustmentTerrainProcessor.apply(Stream.of(
                new TerrainPoint(0, 0, 0), new TerrainPoint(1, 3, 0)
        )).collect(Collectors.toList())).containsExactly(
                new TerrainPoint(1, 1, 0), new TerrainPoint(2, 4, 0)
        );
    }

    @Test
    public void scales() {
        final AdjustmentTerrainProcessor adjustmentTerrainProcessor = AdjustmentTerrainProcessor
                .builder().scale(2).build();

        assertThat(adjustmentTerrainProcessor.apply(Stream.of(
                new TerrainPoint(0, 0, 0), new TerrainPoint(5, 3, 0)
        )).collect(Collectors.toList())).containsExactly(
                new TerrainPoint(0, 0, 0), new TerrainPoint(10, 6, 0)
        );
    }

}