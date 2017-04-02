package com.agh.iet.komplastech.solver.productions.solution.factorization;

import com.agh.iet.komplastech.solver.productions.ProcessingContext;
import com.agh.iet.komplastech.solver.productions.Production;
import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.support.Vertex;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;

import java.io.IOException;

import static com.agh.iet.komplastech.solver.productions.VertexUtils.swapDofsFor;

public class A2_3 implements Production {

    private Mesh mesh;

    @SuppressWarnings("unused")
    public A2_3() {

    }

    public A2_3(Mesh mesh) {
        this.mesh = mesh;
    }

    public void apply(ProcessingContext processingContext) {
        final Vertex currentVertex = processingContext.getVertex();

        final Vertex leftChild = currentVertex.getLeftChild();
        final Vertex middleChild = currentVertex.getMiddleChild();
        final Vertex rightChild = currentVertex.getRightChild();

        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 3; j++) {
                currentVertex.m_a.add(i, j, leftChild.m_a.get(i, j));
                currentVertex.m_a.add(i + 1, j + 1, middleChild.m_a.get(i, j));
                currentVertex.m_a.add(i + 2, j + 2, rightChild.m_a.get(i, j));
            }
            for (int j = 1; j <= mesh.getDofsY(); j++) {
                currentVertex.m_b.add(i, j, leftChild.m_b.get(i, j));
                currentVertex.m_b.add(i + 1, j, middleChild.m_b.get(i, j));
                currentVertex.m_b.add(i + 2, j, rightChild.m_b.get(i, j));
            }
        }
        // bring 3rd degree of freedom to the front
        swapDofsFor(currentVertex, 1, 3, 5, mesh.getDofsY());
        swapDofsFor(currentVertex, 2, 3, 5, mesh.getDofsY());

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

}