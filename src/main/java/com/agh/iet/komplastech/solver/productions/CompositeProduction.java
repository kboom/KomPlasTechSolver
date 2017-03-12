package com.agh.iet.komplastech.solver.productions;

import com.agh.iet.komplastech.solver.support.Vertex;

import java.util.Arrays;
import java.util.List;

public class CompositeProduction implements Production {

    private List<Production> productionList;

    private CompositeProduction(List<Production> productions) {
        this.productionList = productions;
    }

    @Override
    public Vertex apply(ProcessingContext context) {
        productionList.forEach((production) -> production.apply(context));
        return context.getVertex();
    }

    public static CompositeProduction compositeProductionOf(Production... productions) {
        return new CompositeProduction(Arrays.asList(productions));
    }
}
