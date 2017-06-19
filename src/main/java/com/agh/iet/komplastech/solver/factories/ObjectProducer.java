package com.agh.iet.komplastech.solver.factories;

import com.hazelcast.nio.serialization.IdentifiedDataSerializable;

interface ObjectProducer<T extends IdentifiedDataSerializable> {

    T produce();

}
