package com.agh.iet.komplastech.solver;

import com.agh.iet.komplastech.solver.problem.Problem;
import com.agh.iet.komplastech.solver.splines.BSpline1;
import com.agh.iet.komplastech.solver.splines.BSpline2;
import com.agh.iet.komplastech.solver.splines.BSpline3;
import com.agh.iet.komplastech.solver.support.Matrix;
import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.support.Point;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;

import java.io.IOException;

import static com.agh.iet.komplastech.solver.SolutionGrid.solutionGrid;
import static com.agh.iet.komplastech.solver.factories.HazelcastGeneralFactory.GENERAL_FACTORY_ID;
import static com.agh.iet.komplastech.solver.factories.HazelcastGeneralFactory.SOLUTION;
import static com.agh.iet.komplastech.solver.support.Point.solutionPoint;

public class Solution implements IdentifiedDataSerializable {

    private static final BSpline1 b1 = new BSpline1();
    private static final BSpline2 b2 = new BSpline2();
    private static final BSpline3 b3 = new BSpline3();

    private Mesh mesh;
    private Matrix mRHS;
    private transient Problem problem;

    @SuppressWarnings("unused")
    public Solution() {

    }

    public Solution(Problem problem, Mesh mesh, Matrix rhs) {
        this.mesh = mesh;
        this.problem = problem;
        this.mRHS = rhs;
    }

    public SolutionGrid getSolutionGrid() {
        double Dx = mesh.getDx();
        double Dy = mesh.getDy();
        double x = -Dx / 2;
        double y;
        SolutionGrid solutionGrid = solutionGrid();
        for (int i = 1; i <= mesh.getElementsX(); ++i) {
            x += Dx;
            y = -Dy / 2;
            for (int j = 1; j <= mesh.getElementsY(); ++j) {
                y += Dy;
                Point point = solutionPoint(x, y, getValue(x, y));
                solutionGrid.addPoint(point);
            }
        }
        return solutionGrid;
    }

    public double getValue(double x, double y) {
        int ielemx = (int) (x / mesh.getDx()) + 1;
        int ielemy = (int) (y / mesh.getDy()) + 1;
        double localx = x - mesh.getDx() * (ielemx - 1);
        double localy = y - mesh.getDy() * (ielemy - 1);
        double solution = 0.0;
        solution += b1.getValue(localx) * b1.getValue(localy) * mRHS.get(ielemx, ielemy);
        solution += b1.getValue(localx) * b2.getValue(localy) * mRHS.get(ielemx, ielemy + 1);
        solution += b1.getValue(localx) * b3.getValue(localy) * mRHS.get(ielemx, ielemy + 2);
        solution += b2.getValue(localx) * b1.getValue(localy) * mRHS.get(ielemx + 1, ielemy);
        solution += b2.getValue(localx) * b2.getValue(localy) * mRHS.get(ielemx + 1, ielemy + 1);
        solution += b2.getValue(localx) * b3.getValue(localy) * mRHS.get(ielemx + 1, ielemy + 2);
        solution += b3.getValue(localx) * b1.getValue(localy) * mRHS.get(ielemx + 2, ielemy);
        solution += b3.getValue(localx) * b2.getValue(localy) * mRHS.get(ielemx + 2, ielemy + 1);
        solution += b3.getValue(localx) * b3.getValue(localy) * mRHS.get(ielemx + 2, ielemy + 2);
        return solution;
    }

    public Matrix getRhs() {
        return mRHS;
    }

    public double getLaplacian(double x, double y) {
        int ielemx = (int) (x / (mesh.getDx())) + 1;
        int ielemy = (int) (y / (mesh.getDy())) + 1;
        double localx = x - (mesh.getDx()) * (ielemx - 1);
        double localy = y - (mesh.getDy()) * (ielemy - 1);
        double solution = 0.0;
        solution += b1.getSecondDerivativeValueAt(localx) * b1.getValue(localy) * mRHS.get(ielemx, ielemy);
        solution += b1.getSecondDerivativeValueAt(localx) * b2.getValue(localy) * mRHS.get(ielemx, ielemy + 1);
        solution += b1.getSecondDerivativeValueAt(localx) * b3.getValue(localy) * mRHS.get(ielemx, ielemy + 2);
        solution += b2.getSecondDerivativeValueAt(localx) * b1.getValue(localy) * mRHS.get(ielemx + 1, ielemy);
        solution += b2.getSecondDerivativeValueAt(localx) * b2.getValue(localy) * mRHS.get(ielemx + 1, ielemy + 1);
        solution += b2.getSecondDerivativeValueAt(localx) * b3.getValue(localy) * mRHS.get(ielemx + 1, ielemy + 2);
        solution += b3.getSecondDerivativeValueAt(localx) * b1.getValue(localy) * mRHS.get(ielemx + 2, ielemy);
        solution += b3.getSecondDerivativeValueAt(localx) * b2.getValue(localy) * mRHS.get(ielemx + 2, ielemy + 1);
        solution += b3.getSecondDerivativeValueAt(localx) * b3.getValue(localy) * mRHS.get(ielemx + 2, ielemy + 2);

        solution += b1.getValue(localx) * b1.getSecondDerivativeValueAt(localy) * mRHS.get(ielemx, ielemy);
        solution += b1.getValue(localx) * b2.getSecondDerivativeValueAt(localy) * mRHS.get(ielemx, ielemy + 1);
        solution += b1.getValue(localx) * b3.getSecondDerivativeValueAt(localy) * mRHS.get(ielemx, ielemy + 2);
        solution += b2.getValue(localx) * b1.getSecondDerivativeValueAt(localy) * mRHS.get(ielemx + 1, ielemy);
        solution += b2.getValue(localx) * b2.getSecondDerivativeValueAt(localy) * mRHS.get(ielemx + 1, ielemy + 1);
        solution += b2.getValue(localx) * b3.getSecondDerivativeValueAt(localy) * mRHS.get(ielemx + 1, ielemy + 2);
        solution += b3.getValue(localx) * b1.getSecondDerivativeValueAt(localy) * mRHS.get(ielemx + 2, ielemy);
        solution += b3.getValue(localx) * b2.getSecondDerivativeValueAt(localy) * mRHS.get(ielemx + 2, ielemy + 1);
        solution += b3.getValue(localx) * b3.getSecondDerivativeValueAt(localy) * mRHS.get(ielemx + 2, ielemy + 2);

        return solution;
    }

    public Problem getProblem() {
        return problem;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeObject(mesh);
        out.writeObject(mRHS);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        mesh = in.readObject();
        mRHS = in.readObject();
    }

    @Override
    public int getFactoryId() {
        return GENERAL_FACTORY_ID;
    }

    @Override
    public int getId() {
        return SOLUTION;
    }

    public Mesh getMesh() {
        return mesh;
    }
}
