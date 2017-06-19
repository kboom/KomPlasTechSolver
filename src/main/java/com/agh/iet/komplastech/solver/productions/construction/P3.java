package com.agh.iet.komplastech.solver.productions.construction;

import com.agh.iet.komplastech.solver.VertexId;
import com.agh.iet.komplastech.solver.factories.HazelcastProductionFactory.ProductionType;
import com.agh.iet.komplastech.solver.productions.ProcessingContext;
import com.agh.iet.komplastech.solver.productions.Production;
import com.agh.iet.komplastech.solver.support.*;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;

import java.io.IOException;

import static com.agh.iet.komplastech.solver.factories.HazelcastProductionFactory.PRODUCTION_FACTORY;
import static com.agh.iet.komplastech.solver.support.Vertex.aVertex;
import static com.agh.iet.komplastech.solver.support.WeakVertexReference.weakReferenceToVertex;

public class P3 implements Production {

    private VertexRange range;

    @SuppressWarnings("unused")
    public P3() {

    }

    public P3(VertexRange range) {
        this.range = range;
    }

    public void apply(ProcessingContext processingContext) {
        setLeftChild(processingContext);
        setMiddleChild(processingContext);
        setRightChild(processingContext);
        processingContext.updateVertex();
    }

    private void setLeftChild(ProcessingContext processingContext) {
        final Vertex node = processingContext.getVertex();
        final Mesh mesh = processingContext.getMesh();
        final VertexRegionMapper regionMapper = processingContext.getRegionMapper();

        VertexId newVertexId = node.getVertexId().transformed(id ->
                range.getRight() + 3 * (id - range.getLeft()) + 1);
        RegionId regionId = regionMapper.getRegionFor(newVertexId);

        Vertex leftChild = aVertex(newVertexId, regionId)
                .withBeginning(node.beginning)
                .withEnding(node.beginning + (node.ending - node.beginning) / 3.0)
                .inMesh(mesh)
                .build();

        node.setLeftChild(weakReferenceToVertex(leftChild));

        processingContext.storeVertex(leftChild);
    }

    private void setMiddleChild(ProcessingContext processingContext) {
        final Vertex node = processingContext.getVertex();
        final Mesh mesh = processingContext.getMesh();
        final VertexRegionMapper regionMapper = processingContext.getRegionMapper();

        VertexId newVertexId = node.getVertexId().transformed(id ->
                range.getRight() + 3 * (id - range.getLeft()) + 2);
        RegionId regionId = regionMapper.getRegionFor(newVertexId);

        Vertex rightChild = aVertex(newVertexId, regionId)
                .withBeginning(node.beginning + (node.ending - node.beginning) / 3.0)
                .withEnding(node.ending - (node.ending - node.beginning) / 3.0)
                .inMesh(mesh)
                .build();

        node.setMiddleChild(weakReferenceToVertex(rightChild));

        processingContext.storeVertex(rightChild);
    }

    private void setRightChild(ProcessingContext processingContext) {
        final Vertex node = processingContext.getVertex();
        final Mesh mesh = processingContext.getMesh();
        final VertexRegionMapper regionMapper = processingContext.getRegionMapper();

        VertexId newVertexId = node.getVertexId().transformed(id ->
                range.getRight() + 3 * (id - range.getLeft()) + 3);
        RegionId regionId = regionMapper.getRegionFor(newVertexId);

        Vertex rightChild = aVertex(newVertexId, regionId)
                .withBeginning(node.beginning + (node.ending - node.beginning) * 2.0 / 3.0)
                .withEnding(node.ending)
                .inMesh(mesh)
                .build();

        node.setRightChild(weakReferenceToVertex(rightChild));

        processingContext.storeVertex(rightChild);
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeObject(range);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        range = in.readObject();
    }

    @Override
    public int getFactoryId() {
        return PRODUCTION_FACTORY;
    }

    @Override
    public int getId() {
        return ProductionType.P3_PRODUCTION.id;
    }

}