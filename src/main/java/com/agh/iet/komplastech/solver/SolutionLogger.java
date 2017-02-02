package com.agh.iet.komplastech.solver;

import com.agh.iet.komplastech.solver.support.Vertex;

import java.util.Collection;

interface SolutionLogger {

    void logMatrixValues(Iterable<Vertex> vertices, String comment);

    void logValuesOfChildren(Collection<Vertex> parentVertices, String comment);

    void logMatrixValuesAt(Vertex vertex, String message);
}
