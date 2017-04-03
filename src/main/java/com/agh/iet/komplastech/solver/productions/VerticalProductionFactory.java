package com.agh.iet.komplastech.solver.productions;

import com.agh.iet.komplastech.solver.Solution;
import com.agh.iet.komplastech.solver.productions.construction.P1y;
import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.support.VertexRegionMapper;

public class VerticalProductionFactory extends HorizontalProductionFactory {

    private Mesh mesh;
    private VertexRegionMapper vertexRegionMapper;

    public VerticalProductionFactory(
            Mesh mesh,
            Solution horizontalSolution,
            VertexRegionMapper vertexRegionMapper) {
        super(mesh, horizontalSolution.getProblem(), vertexRegionMapper);
        this.mesh = mesh;
        this.vertexRegionMapper = vertexRegionMapper;
    }

    @Override
    public Production createBranchRootProduction() {
        return new P1y(mesh, vertexRegionMapper);
    }

}
