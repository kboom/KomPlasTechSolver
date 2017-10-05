package com.agh.iet.komplastech.solver.terrain;

import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.terrain.processors.CompositeTerrainProcessor;
import com.agh.iet.komplastech.solver.terrain.processors.ToClosestTerrainProcessor;
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

    private final Terraformer terraformer = Terraformer.builder()
            .inputStorage(this)
            .outputStorage(this)
            .terrainProcessor(new CompositeTerrainProcessor(
                    newArrayList(new ToClosestTerrainProcessor())
            ))
            .build();

    private List<TerrainPoint> savedPoints;
    private List<TerrainPoint> originalPoints;

    @Before
    public void prepare() {
        this.savedPoints = new ArrayList<>();
        this.originalPoints = new ArrayList<>();
    }

    @Test
    public void stripsOffsets() {
        originalPoints.addAll(newArrayList(
                new TerrainPoint(1, 0, 1), new TerrainPoint(1, 0, 2),
                new TerrainPoint(2, 1, 3), new TerrainPoint(2, 2, 4)
        ));
        terraformer.terraform(aMesh().withElementsX(2).withElementsY(2).build());
        assertThat(savedPoints).containsExactly(
                new TerrainPoint(0, 0, 1), new TerrainPoint(0, 0, 2),
                new TerrainPoint(1, 1, 3), new TerrainPoint(1, 2, 4)
        );
    }

    @Test
    public void sticksToPoint() {
        originalPoints.addAll(newArrayList(
                new TerrainPoint(1, 0, 1), new TerrainPoint(1, 0, 2), new TerrainPoint(1, 0, 3),
                new TerrainPoint(2, 1, 4), new TerrainPoint(2, 1, 5), new TerrainPoint(2, 1, 6),
                new TerrainPoint(3, 2, 7), new TerrainPoint(3, 2, 8), new TerrainPoint(3, 2, 9)
        ));
        terraformer.terraform(aMesh().withElementsX(3).withElementsY(3).build());
        assertThat(savedPoints).containsExactly(
                new TerrainPoint(0, 0, 1), new TerrainPoint(0, 0, 2), new TerrainPoint(0, 0, 3),
                new TerrainPoint(1, 1, 4), new TerrainPoint(1, 1, 5), new TerrainPoint(1, 1, 6),
                new TerrainPoint(2, 2, 7), new TerrainPoint(2, 2, 8), new TerrainPoint(2, 2, 9)
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

}