package com.agh.iet.komplastech.solver.productions;

import com.agh.iet.komplastech.solver.support.Vertex;

public interface Production {

    Vertex apply(Vertex v);

}