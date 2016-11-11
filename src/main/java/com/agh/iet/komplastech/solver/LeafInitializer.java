package com.agh.iet.komplastech.solver;

import com.agh.iet.komplastech.solver.productions.Production;
import com.agh.iet.komplastech.solver.support.Vertex;

import java.util.List;

interface LeafInitializer {

    List<Production> initializeLeaves(List<Vertex> leafLevelVertices);

}
