package com.agh.iet.komplastech.solver.productions.solution.backsubstitution;

import com.agh.iet.komplastech.solver.productions.ProcessingContext;
import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.support.Vertex;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;

import java.io.IOException;

import static com.agh.iet.komplastech.solver.productions.VertexUtils.swapDofsFor;
import static com.agh.iet.komplastech.solver.factories.HazelcastProductionFactory.BS_2_6_H_PRODUCTION;
import static com.agh.iet.komplastech.solver.factories.HazelcastProductionFactory.PRODUCTION_FACTORY;

public class BS_2_6_H extends PFEProduction {

    private Mesh mesh;

    @SuppressWarnings("unused")
    public BS_2_6_H() {

    }

    public BS_2_6_H(Mesh mesh) {
        this.mesh = mesh;
    }

    public void apply(ProcessingContext processingContext) {
        Vertex T = processingContext.getVertex();

        final Vertex leftChild = T.getLeftChild();
        final Vertex rightChild = T.getRightChild();

        T = partial_backward_substitution(T, 2, 6, mesh.getDofsY());
        swapDofsFor(T, 1, 3, 6, mesh.getDofsY());
        swapDofsFor(T, 2, 4, 6, mesh.getDofsY());

        for (int i = 1; i <= 4; i++) {
            for (int j = 1; j <= mesh.getDofsY(); j++) {
                leftChild.m_x.set(i + 2, j, T.m_x.get(i, j));
                rightChild.m_x.set(i + 2, j, T.m_x.get(i + 2, j));
            }
        }

        processingContext.updateVertexAndChildren();
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
        return BS_2_6_H_PRODUCTION;
    }

}
