package com.agh.iet.komplastech.solver.productions.solution.backsubstitution;

import com.agh.iet.komplastech.solver.productions.ProcessingContext;
import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.support.Vertex;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;

import java.io.IOException;

import static com.agh.iet.komplastech.solver.productions.VertexUtils.swapDofsFor;
import static com.agh.iet.komplastech.solver.factories.HazelcastProductionFactory.BS_1_5_PRODUCTION;
import static com.agh.iet.komplastech.solver.factories.HazelcastProductionFactory.PRODUCTION_FACTORY;

public class BS_1_5 extends PFEProduction {

    private Mesh mesh;

    @SuppressWarnings("unused")
    public BS_1_5() {

    }

    public BS_1_5(Mesh mesh) {
        this.mesh = mesh;
    }

    public void apply(ProcessingContext processingContext) {
        Vertex T = processingContext.getVertex();
        T = partial_backward_substitution(T, 1, 5, mesh.getDofsY());
        swapDofsFor(T, 1, 2, 5, mesh.getDofsY());
        swapDofsFor(T, 2, 3, 5, mesh.getDofsY());
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
        return BS_1_5_PRODUCTION;
    }

}