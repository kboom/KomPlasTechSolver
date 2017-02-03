package com.agh.iet.komplastech.solver;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.unmodifiableList;

class SolutionsInTime {

    private final List<Solution> subsequentSolutions;
    private final double delta;

    SolutionsInTime(List<Solution> subsequentSolutions, double delta) {
        this.subsequentSolutions = unmodifiableList(subsequentSolutions);
        this.delta = delta;
    }

    Solution getFinalSolution() {
        return subsequentSolutions.get(subsequentSolutions.size() - 1);
    }

    static class SolutionsInTimeBuilder {

        private final List<Solution> solutions = new ArrayList<>();
        private double delta;

        void addSolution(Solution solution) {
            solutions.add(solution);
        }

        SolutionsInTimeBuilder withDelta(double delta) {
            this.delta = delta;
            return this;
        }

        public SolutionsInTime build() {
            return new SolutionsInTime(solutions, delta);
        }
    }

    static SolutionsInTimeBuilder solutionsInTime() {
        return new SolutionsInTimeBuilder();
    }

}
