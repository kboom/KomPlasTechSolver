package com.agh.iet.komplastech.solver.productions.initialization;

import com.agh.iet.komplastech.solver.Solution;
import com.agh.iet.komplastech.solver.constants.GaussPoints;
import com.agh.iet.komplastech.solver.problem.Problem;
import com.agh.iet.komplastech.solver.productions.ProcessingContext;
import com.agh.iet.komplastech.solver.productions.Production;
import com.agh.iet.komplastech.solver.splines.BSpline1;
import com.agh.iet.komplastech.solver.splines.BSpline2;
import com.agh.iet.komplastech.solver.splines.BSpline3;
import com.agh.iet.komplastech.solver.splines.Spline;
import com.agh.iet.komplastech.solver.support.Matrix;
import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.support.Vertex;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;

import java.io.IOException;

import static com.agh.iet.komplastech.solver.constants.GaussPoints.GAUSS_POINTS;
import static com.agh.iet.komplastech.solver.constants.GaussPoints.GAUSS_POINT_WEIGHTS;
import static com.agh.iet.komplastech.solver.factories.HazelcastProductionFactory.Ay_PRODUCTION;
import static com.agh.iet.komplastech.solver.factories.HazelcastProductionFactory.PRODUCTION_FACTORY;
import static com.agh.iet.komplastech.solver.productions.initialization.SampleCoefficients.useArbitraryCoefficients;

public class Ay implements Production {

    private static final Spline spline1 = new BSpline1();
    private static final Spline spline2 = new BSpline2();
    private static final Spline spline3 = new BSpline3();

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
//        final Vertex node = processingContext.getVertex();
//        final Solution solution = processingContext.getSolution();
//        final Matrix horizontalX = solution.getRhs();
//        final Mesh mesh = solution.getMesh();

        Mesh mesh = processingContext.getMesh();
        for (int i = 1; i <= mesh.getDofsY(); i++) {
            fillRightHandSide(processingContext, spline3, 1, i);
            fillRightHandSide(processingContext, spline2, 2, i);
            fillRightHandSide(processingContext, spline1, 3, i);
        }

//        final int idx = node.getVertexId().getAbsoluteIndex() - offset;
//        for (int i = 1; i <= mesh.getDofsX(); i++) {
//            node.m_b.set(1, i, partition[0] * horizontalX.get(i, idx + 1));
//            node.m_b.set(2, i, partition[1] * horizontalX.get(i, idx + 2));
//            node.m_b.set(3, i, partition[2] * horizontalX.get(i, idx + 3));
//        }
    }

    private void fillRightHandSide(ProcessingContext context, Spline spline, int r, int i) {
        final Mesh mesh = context.getMesh();
        final Problem problem = context.getProblem();
        final Vertex node = context.getVertex();

        for (int k = 1; k <= GaussPoints.GAUSS_POINT_COUNT; k++) {
            double x = GAUSS_POINTS[k] * mesh.getDx() + node.beginning;
            for (int l = 1; l <= GaussPoints.GAUSS_POINT_COUNT; l++) {
                if (i > 2) {
                    double y = (GAUSS_POINTS[l] + (i - 3)) * mesh.getDy();
                    node.m_b.add(r, i, GAUSS_POINT_WEIGHTS[k] * spline.getValue(GAUSS_POINTS[k]) * GAUSS_POINT_WEIGHTS[l] * spline1.getValue(GAUSS_POINTS[l]) * problem.getValue(x, y));
                }
                if (i > 1 && (i - 2) < mesh.getElementsY()) {
                    double y = (GAUSS_POINTS[l] + (i - 2)) * mesh.getDy();
                    node.m_b.add(r, i, GAUSS_POINT_WEIGHTS[k] * spline.getValue(GAUSS_POINTS[k]) * GAUSS_POINT_WEIGHTS[l] * spline2.getValue(GAUSS_POINTS[l]) * problem.getValue(x, y));
                }
                if ((i - 1) < mesh.getElementsY()) {
                    double y = (GAUSS_POINTS[l] + (i - 1)) * mesh.getDy();
                    node.m_b.add(r, i, GAUSS_POINT_WEIGHTS[k] * spline.getValue(GAUSS_POINTS[k]) * GAUSS_POINT_WEIGHTS[l] * spline3.getValue(GAUSS_POINTS[l]) * problem.getValue(x, y));
                }
            }
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
