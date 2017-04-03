package com.agh.iet.komplastech.solver.support;

import com.agh.iet.komplastech.solver.VertexId;
import com.agh.iet.komplastech.solver.factories.HazelcastGeneralFactory;
import com.agh.iet.komplastech.solver.productions.ProcessingContext;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;

import java.io.IOException;

import static com.agh.iet.komplastech.solver.factories.HazelcastGeneralFactory.GENERAL_FACTORY_ID;
import static com.agh.iet.komplastech.solver.support.RegionId.regionId;

public class VertexRegionMapper implements IdentifiedDataSerializable {

    private ComputeConfig computeConfig;

    @SuppressWarnings("unused")
    public VertexRegionMapper() {
    }

    public VertexRegionMapper(ComputeConfig computeConfig) {
        this.computeConfig = computeConfig;
    }

    // use region of parents for leaves
    // use region of parent for parent children
    // the rest is based on region boundary
    // for now assign the same region
    public RegionId getRegionFor(VertexId vertexId) {
        return regionId(1);
    }

    private boolean isBoundary(ProcessingContext processingContext) {
        Vertex node = processingContext.getVertex();
        int level = VertexRange.forNode(node.getVertexId().getAbsoluteIndex()).getHeight();
//        return level % computeConfig.getRegionHeight() == 0;
        return false;
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
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        computeConfig = in.readObject();
    }

}
