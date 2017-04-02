package com.agh.iet.komplastech.solver.productions;

import java.util.Arrays;
import java.util.List;

public class CompositeProduction implements Production {

    private List<Production> productionList;

    private CompositeProduction(List<Production> productions) {
        this.productionList = productions;
    }

    @Override
    public void apply(ProcessingContext context) {
        productionList.forEach((production) -> production.apply(context));
    }

    public static CompositeProduction compositeProductionOf(Production... productions) {
        return new CompositeProduction(Arrays.asList(productions));
    }
}
