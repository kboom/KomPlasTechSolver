package com.agh.iet.komplastech.solver.productions;

import com.hazelcast.nio.serialization.DataSerializable;

public interface Production extends DataSerializable {

    void apply(ProcessingContext context);

}