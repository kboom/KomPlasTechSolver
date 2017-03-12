package com.agh.iet.komplastech.solver.support;

public interface VertexReference {

    Vertex get();

    void accept(ReferenceVisitor referenceVisitor);

}
