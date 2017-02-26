package com.agh.iet.komplastech.solver.productions.initialization;

import com.agh.iet.komplastech.solver.productions.Production;
import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.support.Vertex;

import static com.agh.iet.komplastech.solver.productions.initialization.SampleCoefficients.useArbitraryCoefficients;

public class Ay extends Production {

    private final double[][] solution;
    private final double[] partition;
    private final int idx;

    public Ay(double[][] solution, double[] partition, int idx, Mesh mesh) {
        super(mesh);
        this.solution = solution;
        this.partition = partition;
        this.idx = idx;
    }

    public Vertex apply(Vertex node) {
        initializeCoefficientsMatrix(node);
        initializeRightHandSides(node);
        return node;
    }

    private void initializeRightHandSides(Vertex node) {
        for (int i = 1; i <= mesh.getDofsX(); i++) {
            node.m_b[1][i] = partition[0] * solution[i][idx + 1];
            node.m_b[2][i] = partition[1] * solution[i][idx + 2];
            node.m_b[3][i] = partition[2] * solution[i][idx + 3];
        }
    }

    private void initializeCoefficientsMatrix(Vertex node) {
        useArbitraryCoefficients(node);
    }

}
