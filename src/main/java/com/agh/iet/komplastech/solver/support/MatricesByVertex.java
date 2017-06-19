package com.agh.iet.komplastech.solver.support;

import com.agh.iet.komplastech.solver.VertexId;
import com.agh.iet.komplastech.solver.factories.HazelcastGeneralFactory.GeneralObjectType;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.agh.iet.komplastech.solver.factories.HazelcastGeneralFactory.GENERAL_FACTORY_ID;

public class MatricesByVertex implements IdentifiedDataSerializable {

    private Map<VertexId, Matrix> matricesByVertex;

    MatricesByVertex(Map<VertexId, Matrix> matricesByVertex) {
        this.matricesByVertex = matricesByVertex;
    }

    @SuppressWarnings("unused")
    public MatricesByVertex() {

    }

    @Override
    public int getFactoryId() {
        return GENERAL_FACTORY_ID;
    }

    @Override
    public int getId() {
        return GeneralObjectType.MATRICES_BY_VERTEX.id;
    }

    public Map<VertexId, Matrix> get() {
        return matricesByVertex;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeInt(matricesByVertex.size());

        matricesByVertex.forEach((v, m) -> {
            try {
                out.writeObject(v);
                out.writeObject(m);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        });
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        final int size = in.readInt();

        matricesByVertex = new HashMap<>(size);
        for(int i = 0; i < size; i++) {
            VertexId vertexId = in.readObject();
            Matrix matrix = in.readObject();
            matricesByVertex.put(vertexId, matrix);
        }
    }
}
