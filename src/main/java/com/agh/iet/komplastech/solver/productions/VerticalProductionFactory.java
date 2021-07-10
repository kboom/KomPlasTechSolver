package com.agh.iet.komplastech.solver.productions;

import com.agh.iet.komplastech.solver.productions.construction.P1y;

public class VerticalProductionFactory extends HorizontalProductionFactory {

    @Override
    public Production createBranchRootProduction() {
        return new P1y();
    }

}
