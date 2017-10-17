package com.agh.iet.komplastech.solver.productions.initialization;

import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.support.Vertex;

public class AyEven extends AyOdd {

    public AyEven(Vertex node, double[][] solution, double[] partition, int idx, Mesh mesh) {
        super(node, solution, partition, idx, mesh);
    }

}
