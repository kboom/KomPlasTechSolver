package com.agh.iet.komplastech.solver.productions.solution.backsubstitution;

import com.agh.iet.komplastech.solver.factories.HazelcastProductionFactory.ProductionType;
import com.agh.iet.komplastech.solver.productions.ProcessingContext;
import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.support.Vertex;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;

import java.io.IOException;

import static com.agh.iet.komplastech.solver.factories.HazelcastProductionFactory.PRODUCTION_FACTORY;
import static com.agh.iet.komplastech.solver.productions.VertexUtils.swapDofsFor;

public class BS_1_5 extends PFEProduction {

    public BS_1_5() {

    }

    public void apply(ProcessingContext processingContext) {
        final Mesh mesh = processingContext.getMesh();
        Vertex T = processingContext.getVertex();

        T = partial_backward_substitution(T, 1, 5, mesh.getDofsY());
        swapDofsFor(T, 1, 2, 5, mesh.getDofsY());
        swapDofsFor(T, 2, 3, 5, mesh.getDofsY());
        processingContext.updateVertex();
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
    }

    @Override
    public int getFactoryId() {
        return PRODUCTION_FACTORY;
    }

    @Override
    public int getId() {
        return ProductionType.BS_1_5_PRODUCTION.id;
    }

}