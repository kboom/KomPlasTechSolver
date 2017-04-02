package com.agh.iet.komplastech.solver.productions.initialization;

import com.agh.iet.komplastech.solver.productions.ProcessingContext;
import com.agh.iet.komplastech.solver.productions.Production;
import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.support.Vertex;

import static com.agh.iet.komplastech.solver.productions.initialization.SampleCoefficients.useArbitraryCoefficients;

public class Ay implements Production {

    private final double[][] solution;
    private final double[] partition;
    private final Mesh mesh;
    private final int offset;

    public Ay(double[][] solution, double[] partition, Mesh mesh, int offset) {
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
        final int idx = node.getId().getAbsoluteIndex() - offset;
        for (int i = 1; i <= mesh.getDofsX(); i++) {
            node.m_b.set(1, i, partition[0] * solution[i][idx + 1]);
            node.m_b.set(2, i, partition[1] * solution[i][idx + 2]);
            node.m_b.set(3, i, partition[2] * solution[i][idx + 3]);
        }
    }

    private void initializeCoefficientsMatrix(ProcessingContext processingContext) {
        useArbitraryCoefficients(processingContext.getVertex());
    }

}
