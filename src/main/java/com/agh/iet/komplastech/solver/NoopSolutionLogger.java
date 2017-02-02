package com.agh.iet.komplastech.solver;

import com.agh.iet.komplastech.solver.support.Vertex;

import java.util.Collection;

public class NoopSolutionLogger implements SolutionLogger {

    @Override
    public void logMatrixValues(Iterable<Vertex> vertices, String comment) {

    }

    @Override
    public void logValuesOfChildren(Collection<Vertex> parentVertices, String comment) {

    }

    @Override
    public void logMatrixValuesAt(Vertex vertex, String message) {

    }

}
