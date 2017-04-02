package com.agh.iet.komplastech.solver;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.util.function.IntFunction;

public class VertexId implements DataSerializable {

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

}
