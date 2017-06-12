package com.agh.iet.komplastech.solver.productions;

import com.agh.iet.komplastech.solver.Solution;
import com.agh.iet.komplastech.solver.problem.Problem;
import com.agh.iet.komplastech.solver.support.ComputeConfig;
import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.support.Vertex;
import com.agh.iet.komplastech.solver.support.VertexRegionMapper;

public interface ProcessingContext {

    Vertex getVertex();

    void storeVertex(Vertex vertex);

    void updateVertex();

    void updateVertexAndChildren();

    Problem getProblem();

    Mesh getMesh();

    ComputeConfig getComputeConfig();

    Solution getSolution();

    VertexRegionMapper getRegionMapper();

}
