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
import static com.agh.iet.komplastech.solver.productions.VertexUtils.swapDofsFor;

public class A2_2 implements Production {

    public A2_2() {

    }

    public void apply(ProcessingContext context) {
        final Mesh mesh = context.getMesh();
        final Vertex currentNode = context.getVertex();
        final Vertex leftChild = currentNode.getLeftChild();
        final Vertex rightChild = currentNode.getRightChild();

        for (int i = 1; i <= 4; i++) {
            for (int j = 1; j <= 4; j++) {
                currentNode.m_a.add(i, j, leftChild.m_a.get(i + 1, j + 1));
                currentNode.m_a.add(i + 2, j + 2, rightChild.m_a.get(i + 1, j + 1));
            }
            for (int j = 1; j <= mesh.getDofsY(); j++) {
                currentNode.m_b.add(i, j, leftChild.m_b.get(i + 1, j));
                currentNode.m_b.add(i + 2, j, rightChild.m_b.get(i + 1, j));
            }
        }
        swapDofsFor(currentNode, 1, 3, 6, mesh.getDofsY());
        swapDofsFor(currentNode, 2, 4, 6, mesh.getDofsY());

        context.updateVertex();
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
        return ProductionType.A2_2_PRODUCTION.id;
    }

}
