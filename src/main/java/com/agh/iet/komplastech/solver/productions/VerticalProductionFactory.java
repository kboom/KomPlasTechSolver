package com.agh.iet.komplastech.solver.productions;

import com.agh.iet.komplastech.solver.productions.construction.P1y;
import com.agh.iet.komplastech.solver.support.VertexRegionMapper;

public class VerticalProductionFactory extends HorizontalProductionFactory {

    private VertexRegionMapper vertexRegionMapper;

    public VerticalProductionFactory(
            VertexRegionMapper vertexRegionMapper) {
        super(vertexRegionMapper);
        this.vertexRegionMapper = vertexRegionMapper;
    }

    @Override
    public Production createBranchRootProduction() {
        return new P1y(vertexRegionMapper);
    }

}
