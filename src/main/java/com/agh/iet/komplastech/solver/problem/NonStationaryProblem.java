package com.agh.iet.komplastech.solver.problem;

import com.agh.iet.komplastech.solver.Solution;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;

public abstract class NonStationaryProblem implements Problem, DataSerializable {

    private double delta;
    private Solution currentSolution;

    @SuppressWarnings("unused")
    public NonStationaryProblem() {

    }


    public NonStationaryProblem(double delta) {
        this.delta = delta;
    }

    public void nextStep(Solution solution) {
        currentSolution = solution;
    }

    @Override
    public double getValue(double x, double y) {
        return currentSolution == null ?
                getInitialValue(x, y) :
                getValueAtTime(x, y, currentSolution, delta);
    }

    protected abstract double getInitialValue(double x, double y);

    protected abstract double getValueAtTime(double x, double y, Solution currentSolution, double delta);

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeDouble(delta);
        out.writeObject(currentSolution);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        delta = in.readDouble();
        currentSolution = in.readObject();
    }

}
