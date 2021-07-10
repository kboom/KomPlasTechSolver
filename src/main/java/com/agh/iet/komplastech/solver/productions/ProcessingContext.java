package com.agh.iet.komplastech.solver.productions;

import com.agh.iet.komplastech.solver.problem.Problem;
import com.agh.iet.komplastech.solver.support.*;

public interface ProcessingContext {

    Vertex getVertex();

    void storeVertex(Vertex vertex);

    void updateVertex();

    void updateVertexAndChildren();

    Problem getProblem();

    Mesh getMesh();

    ComputeConfig getComputeConfig();

    VertexRegionMapper getRegionMapper();

    PartialSolutionManager getPartialSolutionManager();

}
