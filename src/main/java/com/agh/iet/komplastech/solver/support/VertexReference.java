package com.agh.iet.komplastech.solver.support;

import com.agh.iet.komplastech.solver.VertexId;
import com.hazelcast.core.PartitionAware;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;

public interface VertexReference extends PartitionAware, IdentifiedDataSerializable {

    Vertex get();

    VertexId getVertexId();

    RegionId getRegionId();

    void accept(ReferenceVisitor referenceVisitor);

}
