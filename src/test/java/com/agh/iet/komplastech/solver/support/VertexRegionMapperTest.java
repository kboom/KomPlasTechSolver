package com.agh.iet.komplastech.solver.support;


import org.junit.Test;

import static com.agh.iet.komplastech.solver.VertexId.vertexId;
import static com.agh.iet.komplastech.solver.support.Mesh.aMesh;
import static com.agh.iet.komplastech.solver.support.RegionId.regionId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

public class VertexRegionMapperTest {

    @Test
    public void rootAlwaysFormsARegion() {
        VertexRegionMapper vertexRegionMapper = regionMapperOf(24, 2);
        assertThat(vertexRegionMapper.getRegionFor(vertexId(1))).isEqualTo(regionId(1));
    }

    @Test
    public void elementsOnLevelLowerThanRegionHeightUseRootRegion() {
        VertexRegionMapper vertexRegionMapper = regionMapperOf(24, 2);

        assertThat(vertexRegionMapper.getRegionFor(vertexId(2))).isEqualTo(regionId(1));
        assertThat(vertexRegionMapper.getRegionFor(vertexId(3))).isEqualTo(regionId(1));
    }

    @Test
    public void elementsOnRegionBoundaryFormNewRegion() {
        VertexRegionMapper vertexRegionMapper = regionMapperOf(24, 2);

        assertThat(vertexRegionMapper.getRegionFor(vertexId(4))).isEqualTo(regionId(4));
        assertThat(vertexRegionMapper.getRegionFor(vertexId(5))).isEqualTo(regionId(5));
        assertThat(vertexRegionMapper.getRegionFor(vertexId(6))).isEqualTo(regionId(6));
        assertThat(vertexRegionMapper.getRegionFor(vertexId(7))).isEqualTo(regionId(7));
    }

    @Test
    public void leavesAreAssignedToParentRegion() {
        VertexRegionMapper vertexRegionMapper = regionMapperOf(12, 2);

        assertThat(vertexRegionMapper.getRegionFor(vertexId(8))).isEqualTo(regionId(4));
        assertThat(vertexRegionMapper.getRegionFor(vertexId(9))).isEqualTo(regionId(4));
        assertThat(vertexRegionMapper.getRegionFor(vertexId(10))).isEqualTo(regionId(4));

        assertThat(vertexRegionMapper.getRegionFor(vertexId(11))).isEqualTo(regionId(5));
        assertThat(vertexRegionMapper.getRegionFor(vertexId(12))).isEqualTo(regionId(5));
        assertThat(vertexRegionMapper.getRegionFor(vertexId(13))).isEqualTo(regionId(5));

        assertThat(vertexRegionMapper.getRegionFor(vertexId(14))).isEqualTo(regionId(6));
        assertThat(vertexRegionMapper.getRegionFor(vertexId(15))).isEqualTo(regionId(6));
        assertThat(vertexRegionMapper.getRegionFor(vertexId(16))).isEqualTo(regionId(6));

        assertThat(vertexRegionMapper.getRegionFor(vertexId(17))).isEqualTo(regionId(7));
        assertThat(vertexRegionMapper.getRegionFor(vertexId(18))).isEqualTo(regionId(7));
        assertThat(vertexRegionMapper.getRegionFor(vertexId(19))).isEqualTo(regionId(7));
    }

    @Test
    public void vertexDownTheTreeIsAssignedToRootRegionIfRegionHeightDeeper() {
        VertexRegionMapper vertexRegionMapper = regionMapperOf(768, 9);
        assertThat(vertexRegionMapper.getRegionFor(vertexId(256))).isEqualTo(regionId(1));
    }

    @Test
    public void vertexDeepDownTheTreeIsAssignedCorrectRegionAlongWithItsDescendant() {
        VertexRegionMapper vertexRegionMapper = regionMapperOf(768, 7);
        assertThat(vertexRegionMapper.getRegionFor(vertexId(128))).isEqualTo(regionId(128));
        assertThat(vertexRegionMapper.getRegionFor(vertexId(256))).isEqualTo(regionId(128));
    }

    @Test
    public void leafRangeOfBoundaryGetsMappedToVerticesInRange() {
        VertexRegionMapper vertexRegionMapper = regionMapperOf(12, 1);
        assertThat(vertexRegionMapper.getRegionsInRange(VertexRange.range(4, 7)))
                .containsExactly(
                        entry(regionId(4), VertexRange.range(4, 6)),
                        entry(regionId(5), VertexRange.range(4, 6)),
                        entry(regionId(6), VertexRange.range(4, 6)),
                        entry(regionId(7), VertexRange.range(4, 6))
                );
    }
//
//    @Test
//    public void leafRangeGetsMappedToLowestAvailableSetOfRegions() {
//        VertexRegionMapper vertexRegionMapper = regionMapperOf(12, 1);
//        assertThat(vertexRegionMapper.getRegionsInRange(VertexRange.range(8, 19)))
//                .containsExactlyInAnyOrder(regionId(4), regionId(5), regionId(6), regionId(7));
//    }
//
//    @Test
//    public void verticesUnderTheRootGetMappedToRootRegionForRegionHeightEqualToTwo() {
//        VertexRegionMapper vertexRegionMapper = regionMapperOf(12, 2);
//        assertThat(vertexRegionMapper.getRegionsInRange(VertexRange.range(2, 3)))
//                .containsExactly(regionId(1));
//    }

    private VertexRegionMapper regionMapperOf(int problemSize, int regionHeight) {
        ComputeConfig computeConfig = ComputeConfig.aComputeConfig()
                .withRegionHeight(regionHeight)
                .build();


        Mesh mesh = aMesh()
                .withElementsX(problemSize)
                .withElementsY(problemSize)
                .withResolutionX(problemSize)
                .withResolutionY(problemSize)
                .withOrder(2).build();

        return new VertexRegionMapper(mesh, computeConfig);
    }

}