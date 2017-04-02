package com.agh.iet.komplastech.solver.support;

import com.hazelcast.nio.serialization.DataSerializable;

public interface VertexReference extends DataSerializable {

    Vertex get();

    void accept(ReferenceVisitor referenceVisitor);

}
