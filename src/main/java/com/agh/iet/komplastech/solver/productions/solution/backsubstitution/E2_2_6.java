package com.agh.iet.komplastech.solver.productions.solution.backsubstitution;

import com.agh.iet.komplastech.solver.productions.ProcessingContext;
import com.agh.iet.komplastech.solver.support.Mesh;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;

import java.io.IOException;

import static com.agh.iet.komplastech.solver.factories.HazelcastProductionFactory.PRODUCTION_FACTORY;
import static com.agh.iet.komplastech.solver.factories.HazelcastProductionFactory.E2_2_6_PRODUCTION;

public class E2_2_6 extends PFEProduction {

    private Mesh mesh;

    @SuppressWarnings("unused")
    public E2_2_6() {

    }

    public E2_2_6(Mesh mesh) {
        this.mesh = mesh;
    }

    public void apply(ProcessingContext processingContext) {
        partial_forward_elimination(processingContext.getVertex(), 2, 6, mesh.getDofsY());
        processingContext.updateVertex();
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeObject(mesh);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        mesh = in.readObject();
    }

    @Override
    public int getFactoryId() {
        return PRODUCTION_FACTORY;
    }

    @Override
    public int getId() {
        return E2_2_6_PRODUCTION;
    }

}