package com.agh.iet.komplastech.solver.support;

import com.agh.iet.komplastech.solver.VertexId;
import com.agh.iet.komplastech.solver.factories.HazelcastGeneralFactory.GeneralObjectType;
import com.beust.jcommander.internal.Maps;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.IntStream;

import static com.agh.iet.komplastech.solver.VertexId.vertexId;
import static com.agh.iet.komplastech.solver.factories.HazelcastGeneralFactory.GENERAL_FACTORY_ID;
import static com.agh.iet.komplastech.solver.support.RegionId.regionId;
import static java.util.stream.Collectors.toMap;

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

    Map<RegionId, VertexRange> getRegionsInRange(VertexRange range) {
        final int totalHeight = getTreeHeight();
        final int height = range.getHeight();

        if (shouldFormNewRegion(height, totalHeight)) {
            return range.getVerticesInRange().stream()
                    .collect(toMap(
                            v -> regionId(v.getAbsoluteIndex()),
                            rid -> VertexRange.unitary(rid.getAbsoluteIndex()))
                    );
        } else {
            boolean isLeaf = totalHeight == height;
            if (isLeaf) {
                int leftIndexOfPreviousLevel = (int) Math.pow(2, height - 1);
                int rightIndexOfPreviousLevel = 2 * leftIndexOfPreviousLevel;

                return IntStream.range(leftIndexOfPreviousLevel, rightIndexOfPreviousLevel)
                        .mapToObj(RegionId::regionId)
                        .collect(toMap(Function.identity(),
                                rid -> {
                                    int regionId = rid.toInt();
                                    int leftmostVertex = (regionId * 2) + (regionId - leftIndexOfPreviousLevel);
                                    return new VertexRange(leftmostVertex, leftmostVertex + 2);
                                }));
            } else {
                final int relativeHeight = getRelativeHeight(height);
                final int leftOfRelativeHeight = (int) Math.pow(2, relativeHeight);
                return range.getVerticesInRange()
                        .stream()
                        .map(v -> {
                            RegionId region = regionId((int) Math.floor(v.getAbsoluteIndex() / (leftOfRelativeHeight)));
                            return Pair.of(region, v);
                        })
                        .collect(new Collector<Pair<RegionId, VertexId>, Map<RegionId, VertexRange>, Map<RegionId, VertexRange>>() {

                            @Override
                            public Supplier<Map<RegionId, VertexRange>> supplier() {
                                return Maps::newHashMap;
                            }

                            @Override
                            public BiConsumer<Map<RegionId, VertexRange>, Pair<RegionId, VertexId>> accumulator() {
                                return (map, pair) -> {
                                    RegionId region = pair.getKey();
                                    VertexId vertexId = pair.getValue();
                                    map.compute(region, (reg, range) -> (range == null)
                                            ? VertexRange.unitary(vertexId) : range.growToInclude(vertexId));
                                };
                            }

                            @Override
                            public BinaryOperator<Map<RegionId, VertexRange>> combiner() {
                                return null;
                            }

                            @Override
                            public Function<Map<RegionId, VertexRange>, Map<RegionId, VertexRange>> finisher() {
                                return Function.identity();
                            }

                            @Override
                            public Set<Characteristics> characteristics() {
                                return Collections.singleton(Characteristics.IDENTITY_FINISH);
                            }

                        });
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
        return GeneralObjectType.VERTEX_REGION_MAPPER.id;
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
