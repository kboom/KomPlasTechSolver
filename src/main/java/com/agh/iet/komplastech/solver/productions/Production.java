package com.agh.iet.komplastech.solver.productions;

import com.hazelcast.nio.serialization.IdentifiedDataSerializable;

public interface Production extends IdentifiedDataSerializable {

    void apply(ProcessingContext context);

}