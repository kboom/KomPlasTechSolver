package com.agh.iet.komplastech.solver.support;

import com.agh.iet.komplastech.solver.VertexId;
import com.agh.iet.komplastech.solver.factories.HazelcastGeneralFactory;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.agh.iet.komplastech.solver.VertexId.vertexId;
import static com.agh.iet.komplastech.solver.factories.HazelcastGeneralFactory.GENERAL_FACTORY_ID;
import static com.agh.iet.komplastech.solver.support.RegionId.regionId;

public class VertexRegionMapper implements IdentifiedDataSerializable {

    private ComputeConfig computeConfig;
    private Mesh mesh;

    @SuppressWarnings("unused")
    public VertexRegionMapper() {
    }

    public VertexRegionMapper(Mesh mesh, ComputeConfig computeConfig) {
        this.mesh = mesh;
        this.computeConfig = computeConfig;
    }

    public RegionId getRegionFor(VertexId vertexId) {
        final int totalHeight = getTreeHeight();
        final int currentHeight = getCurrentHeight(vertexId);

        if (shouldFormNewRegion(currentHeight, totalHeight)) {
            return regionId(vertexId.getAbsoluteIndex());
        } else {
            int relativeHeight = getRelativeHeight(currentHeight);
            boolean isLeaf = totalHeight == currentHeight;
            if (isLeaf) {
                VertexId parentVertexId = getParentVertexId(vertexId, currentHeight);
                return getRegionFor(parentVertexId);
            } else {
                return regionId((int) Math.floor(vertexId.getAbsoluteIndex() / (Math.pow(2, relativeHeight))));
            }
        }
    }

    Set<RegionId> getRegionsInRange(VertexRange range) {
        final int totalHeight = getTreeHeight();
        final int height = range.getHeight();

        if(shouldFormNewRegion(height, totalHeight)) {
            return range.getVerticesInRange().stream().map(v -> regionId(v.getAbsoluteIndex())).collect(Collectors.toSet());
        } else {
            boolean isLeaf = totalHeight == height;
            if (isLeaf) {
                int leftIndexOfPreviousLevel = (int) Math.pow(2, height - 1);
                int rightIndexOfPreviousLevel = 2 * leftIndexOfPreviousLevel;
                return IntStream.range(leftIndexOfPreviousLevel, rightIndexOfPreviousLevel)
                        .mapToObj(RegionId::regionId).collect(Collectors.toSet());
            } else {
                final int relativeHeight = getRelativeHeight(height);
                final int leftOfRelativeHeight = (int) Math.pow(2, relativeHeight);
                return range.getVerticesInRange().parallelStream().map(v ->
                        regionId((int) Math.floor(v.getAbsoluteIndex() / (leftOfRelativeHeight))))
                        .collect(Collectors.toSet());
            }
        }
    }

    private VertexId getParentVertexId(VertexId vertexId, int currentHeight) {
        VertexRange leafRange = VertexRange.forBinaryAndLastLevel(currentHeight, 3);
        VertexRange parentRange = VertexRange.forBinary(currentHeight - 1);
        int leftOffset = vertexId.getAbsoluteIndex() - leafRange.getLeft();
        return vertexId(parentRange.getLeft() + (int) Math.floor(leftOffset / 3));
    }

    private boolean shouldFormNewRegion(int currentHeight, int totalHeight) {
        int relativeHeight = getRelativeHeight(currentHeight);

        boolean isAtRegionBoundary = relativeHeight == 0;
        boolean isRoot = currentHeight == 0;
        boolean isLeaf = totalHeight == currentHeight;

        return isRoot || (isAtRegionBoundary && !isLeaf);
    }

    private int getCurrentHeight(VertexId vertexId) {
        return VertexRange.forNode(vertexId.getAbsoluteIndex(), mesh.getElementsX()).getHeight();
    }

    private int getTreeHeight() {
        return log2(mesh.getElementsX() / 3) + 1;
    }

    private int log2(double value) {
        return (int) Math.floor(Math.log(value) / Math.log(2));
    }

    private int getRelativeHeight(int currentHeight) {
        return currentHeight % computeConfig.getRegionHeight();
    }

    @Override
    public int getFactoryId() {
        return GENERAL_FACTORY_ID;
    }

    @Override
    public int getId() {
        return HazelcastGeneralFactory.VERTEX_REGION_MAPPER;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeObject(computeConfig);
        out.writeObject(mesh);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        computeConfig = in.readObject();
        mesh = in.readObject();
    }
}
