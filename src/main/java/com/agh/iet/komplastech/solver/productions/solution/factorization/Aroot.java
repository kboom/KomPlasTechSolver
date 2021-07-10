package com.agh.iet.komplastech.solver.productions.solution.factorization;

import com.agh.iet.komplastech.solver.factories.HazelcastProductionFactory.ProductionType;
import com.agh.iet.komplastech.solver.productions.ProcessingContext;
import com.agh.iet.komplastech.solver.productions.Production;
import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.support.Vertex;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;

import java.io.IOException;

import static com.agh.iet.komplastech.solver.factories.HazelcastProductionFactory.PRODUCTION_FACTORY;

public class Aroot implements Production {

    public Aroot() {

    }

    public void apply(ProcessingContext processingContext) {
        final Mesh mesh = processingContext.getMesh();
        final Vertex currentVertex = processingContext.getVertex();

        final Vertex leftChild = currentVertex.getLeftChild();
        final Vertex rightChild = currentVertex.getRightChild();

        for (int i = 1; i <= 4; i++) {
            for (int j = 1; j <= 4; j++) {
                currentVertex.m_a.add(i, j, leftChild.m_a.get(i + 2, j + 2));
                currentVertex.m_a.add(i + 2, j + 2, rightChild.m_a.get(i + 2, j + 2));
            }
            for (int j = 1; j <= mesh.getDofsY(); j++) {
                currentVertex.m_b.add(i, j, leftChild.m_b.get(i + 2, j));
                currentVertex.m_b.add(i + 2, j, rightChild.m_b.get(i + 2, j));
            }
        }

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
        return ProductionType.ARoot_PRODUCTION.id;
    }

}


