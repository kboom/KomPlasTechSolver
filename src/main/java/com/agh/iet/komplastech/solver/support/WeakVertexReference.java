package com.agh.iet.komplastech.solver.support;

import com.agh.iet.komplastech.solver.VertexId;
import com.hazelcast.core.PartitionAware;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;

import java.io.IOException;

import static com.agh.iet.komplastech.solver.factories.HazelcastGeneralFactory.GENERAL_FACTORY_ID;
import static com.agh.iet.komplastech.solver.factories.HazelcastGeneralFactory.WEAK_VERTEX_REFERENCE;

public class WeakVertexReference implements VertexReference, PartitionAware {

    private transient Vertex vertex;

    private VertexId vertexId;
    private RegionId regionId;

    @SuppressWarnings("unused")
    public WeakVertexReference() {

    }

    public WeakVertexReference(Vertex vertex) {
        this.vertexId = vertex.getVertexId();
        this.regionId = vertex.getRegionId();
        this.vertex = vertex;
    }

    public WeakVertexReference(VertexId vertexId, RegionId regionId) {
        this.vertexId = vertexId;
        this.regionId = regionId;
    }

    @Override
    public Vertex get() {
        return vertex;
    }

    @Override
    public VertexId getVertexId() {
        return vertexId;
    }

    @Override
    public RegionId getRegionId() {
        return regionId;
    }

    public static VertexReference weakReferenceToVertex(Vertex to) {
        return new WeakVertexReference(to);
    }

    @Override
    public void accept(ReferenceVisitor referenceVisitor) {
        if (vertex == null) {
            vertex = referenceVisitor.loadVertex(vertexId);
        }
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeObject(vertexId);
        out.writeObject(regionId);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        vertexId = in.readObject();
        regionId = in.readObject();
    }

    @Override
    public int getFactoryId() {
        return GENERAL_FACTORY_ID;
    }

    @Override
    public int getId() {
        return WEAK_VERTEX_REFERENCE;
    }

    @Override
    public Object getPartitionKey() {
        return regionId.toInt();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WeakVertexReference that = (WeakVertexReference) o;

        if (vertexId != null ? !vertexId.equals(that.vertexId) : that.vertexId != null) return false;
        return regionId != null ? regionId.equals(that.regionId) : that.regionId == null;

    }

    @Override
    public int hashCode() {
        int result = vertexId != null ? vertexId.hashCode() : 0;
        result = 31 * result + (regionId != null ? regionId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "WeakVertexReference{" +
                "vertexId=" + vertexId +
                ", regionId=" + regionId +
                '}';
    }

}
