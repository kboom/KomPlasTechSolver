package com.agh.iet.komplastech.solver.productions;

import com.agh.iet.komplastech.solver.problem.Problem;
import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.support.Vertex;

public class LocalProcessingContext implements ProcessingContext {
    public LocalProcessingContext(Vertex p1) {

    }

    @Override
    public Vertex getVertex() {
        return null;
    }

    @Override
    public void storeVertex(Vertex vertex) {

    }

    @Override
    public void updateVertex() {

    }

    @Override
    public void updateVertexAndChildren() {

    }

    @Override
    public Problem getProblem() {
        return null;
    }

    @Override
    public Mesh getMesh() {
        return null;
    }
}
