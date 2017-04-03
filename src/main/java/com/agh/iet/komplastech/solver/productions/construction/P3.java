package com.agh.iet.komplastech.solver.productions.construction;

import com.agh.iet.komplastech.solver.productions.ProcessingContext;
import com.agh.iet.komplastech.solver.productions.Production;
import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.support.Vertex;
import com.agh.iet.komplastech.solver.support.VertexRange;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;

import java.io.IOException;

import static com.agh.iet.komplastech.solver.factories.HazelcastProductionFactory.PRODUCTION_FACTORY;
import static com.agh.iet.komplastech.solver.factories.HazelcastProductionFactory.P3_PRODUCTION;
import static com.agh.iet.komplastech.solver.support.Vertex.aVertex;
import static com.agh.iet.komplastech.solver.support.WeakVertexReference.weakReferenceToVertex;

public class P3 implements Production {

    private Mesh mesh;
    private VertexRange range;

    @SuppressWarnings("unused")
    public P3() {

    }

    public P3(Mesh mesh, VertexRange range) {
        this.mesh = mesh;
        this.range = range;
    }

    public void apply(ProcessingContext processingContext) {
        setLeftChild(processingContext);
        setMiddleChild(processingContext);
        setRightChild(processingContext);
        processingContext.updateVertex();
    }

    private void setLeftChild(ProcessingContext processingContext) {
        Vertex node = processingContext.getVertex();

        Vertex leftChild = aVertex(node.getVertexId().transformed(id -> range.getRight() + 3 * (id - range.getLeft()) + 1))
                .withBeggining(node.beginning)
                .withEnding(node.beginning + (node.ending - node.beginning) / 3.0)
                .inMesh(mesh)
                .build();

        node.setLeftChild(weakReferenceToVertex(leftChild));

        processingContext.storeVertex(leftChild);
    }

    private void setMiddleChild(ProcessingContext processingContext) {
        Vertex node = processingContext.getVertex();

        Vertex rightChild = aVertex(node.getVertexId().transformed(id -> range.getRight() + 3 * (id - range.getLeft()) + 2))
                .withBeggining(node.beginning + (node.ending - node.beginning) / 3.0)
                .withEnding(node.ending - (node.ending - node.beginning) / 3.0)
                .inMesh(mesh)
                .build();

        node.setMiddleChild(weakReferenceToVertex(rightChild));

        processingContext.storeVertex(rightChild);
    }

    private void setRightChild(ProcessingContext processingContext) {
        Vertex node = processingContext.getVertex();

        Vertex rightChild = aVertex(node.getVertexId().transformed(id -> range.getRight() + 3 * (id - range.getLeft()) + 3))
                .withBeggining(node.beginning + (node.ending - node.beginning) * 2.0 / 3.0)
                .withEnding(node.ending)
                .inMesh(mesh)
                .build();

        node.setRightChild(weakReferenceToVertex(rightChild));

        processingContext.storeVertex(rightChild);
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeObject(mesh);
        out.writeObject(range);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        mesh = in.readObject();
        range = in.readObject();
    }

    @Override
    public int getFactoryId() {
        return PRODUCTION_FACTORY;
    }

    @Override
    public int getId() {
        return P3_PRODUCTION;
    }

}