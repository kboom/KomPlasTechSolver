package com.agh.iet.komplastech.solver.productions;

import com.agh.iet.komplastech.solver.support.Vertex;

public interface ProcessingContext {

    Vertex getVertex();

    void storeVertex(Vertex vertex);

}
