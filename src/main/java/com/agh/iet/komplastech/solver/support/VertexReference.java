package com.agh.iet.komplastech.solver.support;

import java.io.Serializable;

public interface VertexReference extends Serializable {

    Vertex get();

    void accept(ReferenceVisitor referenceVisitor);

}
