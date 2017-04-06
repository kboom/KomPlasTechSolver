package com.agh.iet.komplastech.solver.productions.initialization;

import com.agh.iet.komplastech.solver.Solution;
import com.agh.iet.komplastech.solver.productions.ProcessingContext;
import com.agh.iet.komplastech.solver.productions.Production;
import com.agh.iet.komplastech.solver.support.Matrix;
import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.support.Vertex;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;

import java.io.IOException;

import static com.agh.iet.komplastech.solver.factories.HazelcastProductionFactory.Ay_PRODUCTION;
import static com.agh.iet.komplastech.solver.factories.HazelcastProductionFactory.PRODUCTION_FACTORY;
import static com.agh.iet.komplastech.solver.productions.initialization.SampleCoefficients.useArbitraryCoefficients;

public class Ay implements Production {

    private double[] partition;
    private int offset;

    @SuppressWarnings("unused")
    public Ay() {

    }

    public Ay(double[] partition, int offset) {
        this.partition = partition;
        this.offset = offset;
    }

    public void apply(ProcessingContext processingContext) {
        initializeCoefficientsMatrix(processingContext);
        initializeRightHandSides(processingContext);
        processingContext.updateVertex();
    }

    private void initializeRightHandSides(ProcessingContext processingContext) {
        final Vertex node = processingContext.getVertex();
        final Solution solution = processingContext.getSolution();
        final Matrix horizontalX = solution.getRhs();
        final Mesh mesh = solution.getMesh();

        final int idx = node.getVertexId().getAbsoluteIndex() - offset;
        for (int i = 1; i <= mesh.getDofsX(); i++) {
            node.m_b.set(1, i, partition[0] * horizontalX.get(i, idx + 1));
            node.m_b.set(2, i, partition[1] * horizontalX.get(i, idx + 2));
            node.m_b.set(3, i, partition[2] * horizontalX.get(i, idx + 3));
        }
    }

    private void initializeCoefficientsMatrix(ProcessingContext processingContext) {
        useArbitraryCoefficients(processingContext.getVertex());
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeInt(offset);
        out.writeDoubleArray(partition);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        offset = in.readInt();
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
