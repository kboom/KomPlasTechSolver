package com.agh.iet.komplastech.solver;

import com.agh.iet.komplastech.solver.support.Mesh;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.unmodifiableList;

public class SolutionsInTime {

    private final List<Solution> subsequentSolutions;
    private final double delta;
    private Mesh mesh;

    SolutionsInTime(List<Solution> subsequentSolutions, Mesh mesh, double delta) {
        this.subsequentSolutions = unmodifiableList(subsequentSolutions);
        this.mesh = mesh;
        this.delta = delta;
    }

    public Solution getFinalSolution() {
        return subsequentSolutions.get(subsequentSolutions.size() - 1);
    }

    public int getProblemSize() {
        return mesh.getElementsX(); // todo for now
    }

    public int getTimeStepCount() {
        return subsequentSolutions.size();
    }

    public Solution getSolutionAt(int timeStep) {
        return subsequentSolutions.get(timeStep);
    }

    static class SolutionsInTimeBuilder {

        private final List<Solution> solutions = new ArrayList<>();
        private double delta;
        private Mesh mesh;

        SolutionsInTimeBuilder withMesh(Mesh mesh) {
            this.mesh = mesh;
            return this;
        }

        SolutionsInTimeBuilder withDelta(double delta) {
            this.delta = delta;
            return this;
        }

        SolutionsInTimeBuilder addSolution(Solution solution) {
            solutions.add(solution);
            return this;
        }

        public SolutionsInTime build() {
            return new SolutionsInTime(solutions, mesh, delta);
        }
    }

    static SolutionsInTimeBuilder solutionsInTime() {
        return new SolutionsInTimeBuilder();
    }

}
