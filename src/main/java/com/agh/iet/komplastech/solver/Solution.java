package com.agh.iet.komplastech.solver;

import com.agh.iet.komplastech.solver.factories.HazelcastGeneralFactory.GeneralObjectType;
import com.agh.iet.komplastech.solver.problem.Problem;
import com.agh.iet.komplastech.solver.splines.BSpline1;
import com.agh.iet.komplastech.solver.splines.BSpline2;
import com.agh.iet.komplastech.solver.splines.BSpline3;
import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.support.PartialSolutionManager;
import com.agh.iet.komplastech.solver.support.Point;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import static com.agh.iet.komplastech.solver.SolutionGrid.solutionGrid;
import static com.agh.iet.komplastech.solver.factories.HazelcastGeneralFactory.GENERAL_FACTORY_ID;
import static com.agh.iet.komplastech.solver.support.Point.solutionPoint;

public class Solution implements HazelcastInstanceAware, IdentifiedDataSerializable {

    private static final BSpline1 b1 = new BSpline1();
    private static final BSpline2 b2 = new BSpline2();
    private static final BSpline3 b3 = new BSpline3();

    private Mesh mesh; // todo remove from here, it is cached at the client side!
    private transient PartialSolutionManager partialSolutionManager;
    private Problem problem;
    private RunInformation runInformation;

    @SuppressWarnings("unused")
    public Solution() {

    }

    public Solution(Problem problem, Mesh mesh, PartialSolutionManager rhs, RunInformation runInformation) {
        this.mesh = mesh;
        this.problem = problem;
        this.partialSolutionManager = rhs;
        this.runInformation = runInformation;
    }

    public double getValue(double x, double y) {
        int ielemx = (int) (x / mesh.getDx()) + 1;
        int ielemy = (int) (y / mesh.getDy()) + 1;
        double localx = x - mesh.getDx() * (ielemx - 1);
        double localy = y - mesh.getDy() * (ielemy - 1);
        double solution = 0.0;

        double[][] rows = partialSolutionManager.getRows(ielemx, ielemx + 1, ielemx + 2);
        solution += b1.getValue(localx) * b1.getValue(localy) * rows[0][ielemy];
        solution += b1.getValue(localx) * b2.getValue(localy) * rows[0][ielemy + 1];
        solution += b1.getValue(localx) * b3.getValue(localy) * rows[0][ielemy + 2];
        solution += b2.getValue(localx) * b1.getValue(localy) * rows[1][ielemy];
        solution += b2.getValue(localx) * b2.getValue(localy) * rows[1][ielemy + 1];
        solution += b2.getValue(localx) * b3.getValue(localy) * rows[1][ielemy + 2];
        solution += b3.getValue(localx) * b1.getValue(localy) * rows[2][ielemy];
        solution += b3.getValue(localx) * b2.getValue(localy) * rows[2][ielemy + 1];
        solution += b3.getValue(localx) * b3.getValue(localy) * rows[2][ielemy + 2];
        return solution;
    }

    public double getLaplacian(double x, double y) {
        int ielemx = (int) (x / (mesh.getDx())) + 1;
        int ielemy = (int) (y / (mesh.getDy())) + 1;
        double localx = x - (mesh.getDx()) * (ielemx - 1);
        double localy = y - (mesh.getDy()) * (ielemy - 1);
        double solution = 0.0;

        double[][] rows = partialSolutionManager.getRows(ielemx, ielemx + 1, ielemx + 2);

        if(!isEven()) {
            solution += b1.getSecondDerivativeValueAt(localx) * b1.getValue(localy) * rows[0][ielemy];
            solution += b1.getSecondDerivativeValueAt(localx) * b2.getValue(localy) * rows[0][ielemy + 1];
            solution += b1.getSecondDerivativeValueAt(localx) * b3.getValue(localy) * rows[0][ielemy + 2];
            solution += b2.getSecondDerivativeValueAt(localx) * b1.getValue(localy) * rows[1][ielemy];
            solution += b2.getSecondDerivativeValueAt(localx) * b2.getValue(localy) * rows[1][ielemy + 1];
            solution += b2.getSecondDerivativeValueAt(localx) * b3.getValue(localy) * rows[1][ielemy + 2];
            solution += b3.getSecondDerivativeValueAt(localx) * b1.getValue(localy) * rows[2][ielemy];
            solution += b3.getSecondDerivativeValueAt(localx) * b2.getValue(localy) * rows[2][ielemy + 1];
            solution += b3.getSecondDerivativeValueAt(localx) * b3.getValue(localy) * rows[2][ielemy + 2];

        } else {
            solution += b1.getValue(localx) * b1.getSecondDerivativeValueAt(localy) * rows[0][ielemy];
            solution += b1.getValue(localx) * b2.getSecondDerivativeValueAt(localy) * rows[0][ielemy + 1];
            solution += b1.getValue(localx) * b3.getSecondDerivativeValueAt(localy) * rows[0][ielemy + 2];
            solution += b2.getValue(localx) * b1.getSecondDerivativeValueAt(localy) * rows[1][ielemy];
            solution += b2.getValue(localx) * b2.getSecondDerivativeValueAt(localy) * rows[1][ielemy + 1];
            solution += b2.getValue(localx) * b3.getSecondDerivativeValueAt(localy) * rows[1][ielemy + 2];
            solution += b3.getValue(localx) * b1.getSecondDerivativeValueAt(localy) * rows[2][ielemy];
            solution += b3.getValue(localx) * b2.getSecondDerivativeValueAt(localy) * rows[2][ielemy + 1];
            solution += b3.getValue(localx) * b3.getSecondDerivativeValueAt(localy) * rows[2][ielemy + 2];

        }

        return solution;
    }

    private boolean isEven() {
        return runInformation.getRunNumber() % 2 == 0;
    }

    SolutionGrid getSolutionGrid() {
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

    public Problem getProblem() {
        return problem;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeObject(mesh);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        mesh = in.readObject();
    }

    @Override
    public int getFactoryId() {
        return GENERAL_FACTORY_ID;
    }

    @Override
    public int getId() {
        return GeneralObjectType.SOLUTION.id;
    }

    public Mesh getMesh() {
        return mesh;
    }

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        partialSolutionManager = new PartialSolutionManager(getMesh(), hazelcastInstance);
    }

    /**
     * Returns an approximate checksum of the solution.
     * Note that this is only verifying central row and column as the result might be too big to retrieve and process on a single node.
     */
    int getChecksum() {
        int xHash = Arrays.hashCode(partialSolutionManager.getCols(mesh.getCenterX())[0]);
        int yHash = Arrays.hashCode(partialSolutionManager.getRows(mesh.getCenterY())[0]);
        return Objects.hash(xHash, yHash);
    }

}
