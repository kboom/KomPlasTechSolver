package com.agh.iet.komplastech.solver.support;

import com.hazelcast.nio.serialization.IdentifiedDataSerializable;

public interface VertexReference extends IdentifiedDataSerializable {

    Vertex get();

    void accept(ReferenceVisitor referenceVisitor);

}
