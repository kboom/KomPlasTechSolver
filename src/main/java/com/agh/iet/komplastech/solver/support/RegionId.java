package com.agh.iet.komplastech.solver.support;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;

import java.io.IOException;

import static com.agh.iet.komplastech.solver.factories.HazelcastGeneralFactory.GENERAL_FACTORY_ID;
import static com.agh.iet.komplastech.solver.factories.HazelcastGeneralFactory.REGION_ID;

public class RegionId implements IdentifiedDataSerializable {

    private int regionId;

    public RegionId(int regionId) {
        this.regionId = regionId;
    }

    @Override
    public int getFactoryId() {
        return GENERAL_FACTORY_ID;
    }

    public int asInt() {
        return regionId;
    }

    @Override
    public int getId() {
        return REGION_ID;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeInt(regionId);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        regionId = in.readInt();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RegionId regionId1 = (RegionId) o;

        return regionId == regionId1.regionId;

    }

    @Override
    public int hashCode() {
        return regionId;
    }

    public static RegionId regionId(int regionId) {
        return new RegionId(regionId);
    }

    @Override
    public String toString() {
        return "RegionId{" +
                "regionId=" + regionId +
                '}';
    }

    @SuppressWarnings("unused")
    public RegionId() {

    }

    public int toInt() {
        return regionId;
    }

}
