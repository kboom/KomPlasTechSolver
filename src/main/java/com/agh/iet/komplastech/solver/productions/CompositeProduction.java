package com.agh.iet.komplastech.solver.productions;

import com.agh.iet.komplastech.solver.factories.HazelcastProductionFactory.ProductionType;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.agh.iet.komplastech.solver.factories.HazelcastProductionFactory.PRODUCTION_FACTORY;

public class CompositeProduction implements Production {

    private List<Production> productionList;

    @SuppressWarnings("unused")
    public CompositeProduction() {

    }

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

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeInt(productionList.size());
        productionList.forEach(element -> {
            try {
                out.writeObject(element);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        });
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        int productionCount = in.readInt();
        productionList = new ArrayList<>(productionCount);
        for(int i = 0; i < productionCount; i++) {
            productionList.add(in.readObject());
        }
    }

    @Override
    public int getFactoryId() {
        return PRODUCTION_FACTORY;
    }

    @Override
    public int getId() {
        return ProductionType.COMPOSITE_PRODUCTION.id;
    }
}
