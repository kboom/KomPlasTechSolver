package com.agh.iet.komplastech.solver.initialization;

import com.agh.iet.komplastech.solver.productions.Production;
import com.agh.iet.komplastech.solver.support.Vertex;

import java.util.List;

public interface LeafInitializer {

    List<Production> initializeLeaves(List<Vertex> leafLevelVertices);

}
