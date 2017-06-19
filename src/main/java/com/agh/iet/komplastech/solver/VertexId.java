package com.agh.iet.komplastech.solver;

import com.agh.iet.komplastech.solver.factories.HazelcastGeneralFactory.GeneralObjectType;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;

import java.io.IOException;
import java.util.function.IntFunction;

import static com.agh.iet.komplastech.solver.factories.HazelcastGeneralFactory.GENERAL_FACTORY_ID;

public class VertexId implements IdentifiedDataSerializable {

    private int id;

    @SuppressWarnings("unused")
    public VertexId() {

    }

    private VertexId(int id) {
        this.id = id;
    }

    public static VertexId vertexId(int id) {
        return new VertexId(id);
    }

    public VertexId transformed(IntFunction<Integer> transformer) {
        return vertexId(transformer.apply(this.id));
    }

    public int getAbsoluteIndex() {
        return id;
    }

    @Override
    public String toString() {
        return "VertexId{" +
                "id=" + id +
                '}';
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeInt(id);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        id = in.readInt();
    }

    @Override
    public int getFactoryId() {
        return GENERAL_FACTORY_ID;
    }

    @Override
    public int getId() {
        return GeneralObjectType.VERTEX_ID.id;
    }

}
