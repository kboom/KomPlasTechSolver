package com.agh.iet.komplastech.solver.storage;

import com.agh.iet.komplastech.solver.support.Vertex;
import com.agh.iet.komplastech.solver.support.VertexMap;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;

import java.io.IOException;

public class InMemoryObjectStore implements ObjectStore {
    @Override
    public void storeVertex(Vertex vertex) {

    }

    @Override
    public VertexMap getVertexMap() {
        return null;
    }

    @Override
    public void clearAll() {

    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {

    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {

    }
}
