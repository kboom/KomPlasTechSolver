package com.agh.iet.komplastech.solver.productions.initialization;

import com.agh.iet.komplastech.solver.productions.ProcessingContext;
import com.agh.iet.komplastech.solver.productions.Production;
import com.agh.iet.komplastech.solver.support.Matrix;
import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.support.Vertex;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;

import java.io.IOException;

import static com.agh.iet.komplastech.solver.productions.initialization.SampleCoefficients.useArbitraryCoefficients;
import static com.agh.iet.komplastech.solver.factories.HazelcastProductionFactory.Ay_PRODUCTION;
import static com.agh.iet.komplastech.solver.factories.HazelcastProductionFactory.PRODUCTION_FACTORY;

public class Ay implements Production {

    private Matrix solution;
    private double[] partition;
    private Mesh mesh;
    private int offset;

    @SuppressWarnings("unused")
    public Ay() {

    }

    public Ay(Matrix solution, double[] partition, Mesh mesh, int offset) {
        this.solution = solution;
        this.partition = partition;
        this.mesh = mesh;
        this.offset = offset;
    }

    public void apply(ProcessingContext processingContext) {
        initializeCoefficientsMatrix(processingContext);
        initializeRightHandSides(processingContext);
        processingContext.updateVertex();
    }

    private void initializeRightHandSides(ProcessingContext processingContext) {
        Vertex node = processingContext.getVertex();
        final int idx = node.getVertexId().getAbsoluteIndex() - offset;
        for (int i = 1; i <= mesh.getDofsX(); i++) {
            node.m_b.set(1, i, partition[0] * solution.get(i, idx + 1));
            node.m_b.set(2, i, partition[1] * solution.get(i, idx + 2));
            node.m_b.set(3, i, partition[2] * solution.get(i, idx + 3));
        }
    }

    private void initializeCoefficientsMatrix(ProcessingContext processingContext) {
        useArbitraryCoefficients(processingContext.getVertex());
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeObject(mesh);
        out.writeInt(offset);
        out.writeObject(solution);
        out.writeDoubleArray(partition);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        mesh = in.readObject();
        offset = in.readInt();
        solution = in.readObject();
        partition = in.readDoubleArray();
    }

    @Override
    public int getFactoryId() {
        return PRODUCTION_FACTORY;
    }

    @Override
    public int getId() {
        return Ay_PRODUCTION;
    }

}
