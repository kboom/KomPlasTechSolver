package com.agh.iet.komplastech.solver.logger;

import com.agh.iet.komplastech.solver.VertexId;
import com.agh.iet.komplastech.solver.support.VertexRange;

public class NoopSolutionLogger implements SolutionLogger {

    @Override
    public void logMatrixValuesAt(VertexId vertex, String message) {

    }

    @Override
    public void logMatrixValuesFor(VertexRange vertexRange, String message) {

    }

}
