package com.agh.iet.komplastech.solver.terrain;

import com.agh.iet.komplastech.solver.terrain.processors.CompositeTerrainProcessor;
import com.agh.iet.komplastech.solver.terrain.processors.RegularMeshTerrainProcessor;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.newArrayList;

public class TerraformerTest implements TerrainStorage {

    private final Terraformer terraformer = Terraformer.builder()
            .inputStorage(this)
            .outputStorage(this)
            .terrainProcessor(new CompositeTerrainProcessor(
                    newArrayList(new RegularMeshTerrainProcessor())
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
    public void canTerraform() {
        originalPoints.addAll(newArrayList(new TerrainPoint(0, 0, 0)));
        terraformer.terraform();
        assertThat(savedPoints).containsExactly(new TerrainPoint(0, 0, 0));
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