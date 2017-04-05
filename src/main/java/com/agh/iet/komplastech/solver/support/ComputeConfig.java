package com.agh.iet.komplastech.solver.support;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;

import java.io.IOException;

import static com.agh.iet.komplastech.solver.factories.HazelcastGeneralFactory.COMPUTE_CONFIG;
import static com.agh.iet.komplastech.solver.factories.HazelcastGeneralFactory.GENERAL_FACTORY_ID;

public class ComputeConfig implements IdentifiedDataSerializable {

    private int regionHeight;
    private int maxBatchSize;

    @Override
    public int getFactoryId() {
        return GENERAL_FACTORY_ID;
    }

    @Override
    public int getId() {
        return COMPUTE_CONFIG;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeInt(regionHeight);
        out.writeInt(maxBatchSize);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        regionHeight = in.readInt();
        maxBatchSize = in.readInt();
    }

    public int getRegionHeight() {
        return regionHeight;
    }

    public int getMaxBatchSize() { return maxBatchSize; }

    public static class ComputeConfigBuilder {

        private ComputeConfig computeConfig = new ComputeConfig();

        public ComputeConfigBuilder withRegionHeight(int regionHeight) {
            computeConfig.regionHeight = regionHeight;
            return this;
        }

        public ComputeConfig build() {
            return computeConfig;
        }

        public ComputeConfigBuilder withMaxBatchSize(int maxBatchSize) {
            computeConfig.maxBatchSize = maxBatchSize;
            return this;
        }
    }

    public static ComputeConfigBuilder aComputeConfig() {
        return new ComputeConfigBuilder();
    }

}
