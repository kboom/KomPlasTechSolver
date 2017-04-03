package com.agh.iet.komplastech.solver.productions.construction;

import com.agh.iet.komplastech.solver.productions.ProcessingContext;
import com.agh.iet.komplastech.solver.productions.Production;
import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.support.Vertex;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;

import java.io.IOException;
import java.util.function.IntFunction;

import static com.agh.iet.komplastech.solver.factories.HazelcastProductionFactory.PRODUCTION_FACTORY;
import static com.agh.iet.komplastech.solver.factories.HazelcastProductionFactory.P2_PRODUCTION;
import static com.agh.iet.komplastech.solver.support.Vertex.aVertex;
import static com.agh.iet.komplastech.solver.support.WeakVertexReference.weakReferenceToVertex;

public class P2 implements Production {

    private static final IntFunction<Integer> LEFT_CHILD_OF_INTERMEDIATE = (id) -> 2 * id;
    private static final IntFunction<Integer> RIGHT_CHILD_OF_INTERMEDIATE = (id) -> 2 * id + 1;

    private Mesh mesh;

    @SuppressWarnings("unused")
    public P2() {

    }

    public P2(Mesh mesh) {
        this.mesh = mesh;
    }

    public void apply(ProcessingContext processingContext) {
        setLeftChild(processingContext);
        setRightChild(processingContext);
        processingContext.updateVertex();
    }

    private void setLeftChild(ProcessingContext processingContext) {
        Vertex node = processingContext.getVertex();

        Vertex leftChild = aVertex(node.getVertexId().transformed(LEFT_CHILD_OF_INTERMEDIATE))
                .withBeggining(node.beginning)
                .withEnding(node.beginning + (node.ending - node.beginning) * 0.5)
                .inMesh(mesh)
                .build();

        node.setLeftChild(weakReferenceToVertex(leftChild));

        processingContext.storeVertex(leftChild);
    }

    private void setRightChild(ProcessingContext processingContext) {
        Vertex node = processingContext.getVertex();

        Vertex rightChild = aVertex(node.getVertexId().transformed(RIGHT_CHILD_OF_INTERMEDIATE))
                .withBeggining(node.beginning + (node.ending - node.beginning) * 0.5)
                .withEnding(node.ending)
                .inMesh(mesh)
                .build();

        node.setRightChild(weakReferenceToVertex(rightChild));

        processingContext.storeVertex(rightChild);
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
        return P2_PRODUCTION;
    }

}
