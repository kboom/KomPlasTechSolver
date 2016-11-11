package com.agh.iet.komplastech.solver.productions;

import com.agh.iet.komplastech.solver.Solution;
import com.agh.iet.komplastech.solver.productions.HorizontalProductionFactory;
import com.agh.iet.komplastech.solver.productions.construction.P1y;
import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.support.Vertex;

public class VerticalProductionFactory extends HorizontalProductionFactory {

    private Solution solution;
    private Mesh mesh;

    public VerticalProductionFactory(Mesh mesh, Solution solution) {
        super(mesh);
        this.mesh = mesh;
        this.solution = solution;
    }


    @Override
    public Production createRootProduction(Vertex vertex) {
        return new P1y(vertex, mesh);
    }

}
