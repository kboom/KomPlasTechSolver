package com.agh.iet.komplastech.solver.productions.solution.backsubstitution;

import com.agh.iet.komplastech.solver.productions.ProcessingContext;
import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.support.Vertex;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;

import java.io.IOException;

import static com.agh.iet.komplastech.solver.productions.VertexUtils.swapDofsFor;

public class BS_2_6 extends PFEProduction {

    private Mesh mesh;

    @SuppressWarnings("unused")
    public BS_2_6() {

    }

    public BS_2_6(Mesh mesh) {
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
                leftChild.m_x.set(i + 1, j, T.m_x.get(i, j));
                rightChild.m_x.set(i + 1, j, T.m_x.get(i + 2, j));
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

}