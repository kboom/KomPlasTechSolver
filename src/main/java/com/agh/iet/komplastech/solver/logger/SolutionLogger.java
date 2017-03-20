package com.agh.iet.komplastech.solver.logger;

import com.agh.iet.komplastech.solver.VertexId;
import com.agh.iet.komplastech.solver.support.VertexRange;

public interface SolutionLogger {

    void logMatrixValuesAt(VertexId vertex, String message);

    void logMatrixValuesFor(VertexRange vertexRange, String message);

}
