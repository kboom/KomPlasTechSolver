package com.agh.iet.komplastech.solver.problem;

import com.agh.iet.komplastech.solver.Solution;
import com.agh.iet.komplastech.solver.support.Mesh;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;

import java.io.IOException;

import static com.agh.iet.komplastech.solver.factories.HazelcastProblemFactory.HEAT;

public class HeatTransferProblem extends NonStationaryProblem {

    private int problemSize;
    private Mesh mesh;

    @SuppressWarnings("unused")
    public HeatTransferProblem() {

    }

    public HeatTransferProblem(double delta, Mesh mesh, int problemSize) {
        super(delta);
        this.mesh = mesh;
        this.problemSize = problemSize;
    }

    @Override
    protected double getInitialValue(double x, double y) {
        double dist = (x - mesh.getCenterX()) * (x - mesh.getCenterX())
                + (y - mesh.getCenterY()) * (y - mesh.getCenterY());

        return dist < problemSize ? problemSize - dist : 0;
    }

    @Override
    protected double getValueAtTime(double x, double y, Solution currentSolution, double delta) {
        double value = currentSolution.getValue(x, y);
        return value + delta * currentSolution.getLaplacian(x, y);
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        super.writeData(out);
        out.writeInt(problemSize);
        out.writeObject(mesh);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        super.readData(in);
        problemSize = in.readInt();
        mesh = in.readObject();
    }

    @Override
    public int getId() {
        return HEAT;
    }

}
