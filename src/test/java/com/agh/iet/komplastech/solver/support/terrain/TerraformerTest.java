package com.agh.iet.komplastech.solver.support.terrain;

import com.agh.iet.komplastech.solver.support.terrain.processors.AdjustmentTerrainProcessor;
import com.agh.iet.komplastech.solver.support.terrain.processors.ChainedTerrainProcessor;
import com.agh.iet.komplastech.solver.support.terrain.processors.ToClosestTerrainProcessor;
import com.agh.iet.komplastech.solver.support.terrain.storage.TerrainStorage;
import com.agh.iet.komplastech.solver.support.terrain.support.Point2D;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.agh.iet.komplastech.solver.support.Mesh.aMesh;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.newArrayList;

public class TerraformerTest implements TerrainStorage {

    private List<TerrainPoint> savedPoints;
    private List<TerrainPoint> originalPoints;

    @Before
    public void prepare() {
        this.savedPoints = new ArrayList<>();
        this.originalPoints = new ArrayList<>();
    }

    @Test
    public void isExactForExactMatch() {
        final Terraformer terraformer = buildAligned(0, 0, 1);

        originalPoints.addAll(newArrayList(
                new TerrainPoint(0, 0, 1), new TerrainPoint(0, 1, 2),
                new TerrainPoint(1, 0, 3), new TerrainPoint(1, 1, 4)
        ));

        terraformer.terraform(aMesh().withElementsX(2).withElementsY(2).build());

        assertThat(savedPoints).containsExactlyInAnyOrder(
                new TerrainPoint(0, 0, 1), new TerrainPoint(0, 1, 2),
                new TerrainPoint(1, 0, 3), new TerrainPoint(1, 1, 4)
        );
    }

    @Test
    public void isExactForExactMatchWithOffset() {
        final Terraformer terraformer = buildAligned(100, 100, 1);

        originalPoints.addAll(newArrayList(
                new TerrainPoint(100, 100, 1), new TerrainPoint(100, 101, 2),
                new TerrainPoint(101, 100, 3), new TerrainPoint(101, 101, 4)
        ));

        terraformer.terraform(aMesh().withElementsX(2).withElementsY(2).build());

        assertThat(savedPoints).containsExactlyInAnyOrder(
                new TerrainPoint(0, 0, 1), new TerrainPoint(0, 1, 2),
                new TerrainPoint(1, 0, 3), new TerrainPoint(1, 1, 4)
        );
    }

    @Test
    public void snapsToClosestPointsValues() {
        final Terraformer terraformer = buildAligned(0, 0, 1);

        originalPoints.addAll(newArrayList(
                new TerrainPoint(3, 4, 101),
                new TerrainPoint(4, 3, 303)
        ));

        terraformer.terraform(aMesh().withElementsX(2).withElementsY(2).build());

        assertThat(savedPoints).containsExactlyInAnyOrder(
                new TerrainPoint(0, 0, 101), new TerrainPoint(0, 1, 101),
                new TerrainPoint(1, 0, 303), new TerrainPoint(1, 1, 101)
        );
    }

    @Override
    public Stream<TerrainPoint> loadTerrainPoints() {
        return this.originalPoints.stream();
    }

    @Override
    public void saveTerrainPoints(Stream<TerrainPoint> terrainPointStream) {
        this.savedPoints = terrainPointStream.collect(Collectors.toList());
    }

    private Terraformer buildAligned(double x, double y, double scale) {
        return Terraformer.builder()
                .inputStorage(this)
                .outputStorage(this)
                .terrainProcessor(
                        ChainedTerrainProcessor.startingFrom(AdjustmentTerrainProcessor.builder().center(new Point2D(x, y)).scale(scale).build())
                                .withNext(new ToClosestTerrainProcessor())
                                .withNext(AdjustmentTerrainProcessor.builder().center(new Point2D(-x, -y)).scale(1/scale).build())
                )
                .build();
    }

}