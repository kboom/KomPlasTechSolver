package com.agh.iet.komplastech.solver.splines;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;

public abstract class Spline implements DataSerializable {

    private static final int VALUE_FOR_OUTSIDE_DOMAIN = 0;

    private int lowerDomainBound;

    private int upperDomainBound;

    public Spline(int lowerDomainBound, int upperDomainBound) {
        this.lowerDomainBound = lowerDomainBound;
        this.upperDomainBound = upperDomainBound;
    }

    public double getValue(double x) {
        if (belongsToDomain(x)) {
            return getFunctionValue(x);
        } else return VALUE_FOR_OUTSIDE_DOMAIN;
    }

    protected abstract double getFunctionValue(double x);

    public abstract double getSecondDerivativeValueAt(double x);

    private boolean belongsToDomain(double x) {
        return x >= lowerDomainBound && x <= upperDomainBound;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeInt(lowerDomainBound);
        out.writeInt(upperDomainBound);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        lowerDomainBound = in.readInt();
        upperDomainBound = in.readInt();
    }

}
