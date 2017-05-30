package com.agh.iet.komplastech.solver.productions.construction;

import com.agh.iet.komplastech.solver.VertexId;
import com.agh.iet.komplastech.solver.productions.ProcessingContext;
import com.agh.iet.komplastech.solver.productions.Production;
import com.agh.iet.komplastech.solver.support.*;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;

import java.io.IOException;

import static com.agh.iet.komplastech.solver.factories.HazelcastProductionFactory.P3_PRODUCTION;
import static com.agh.iet.komplastech.solver.factories.HazelcastProductionFactory.PRODUCTION_FACTORY;
import static com.agh.iet.komplastech.solver.support.Vertex.aVertex;
import static com.agh.iet.komplastech.solver.support.WeakVertexReference.weakReferenceToVertex;

public class P3 implements Production {

    private VertexRange range;
    private VertexRegionMapper vertexRegionMapper;

    @SuppressWarnings("unused")
    public P3() {

    }

    public P3(VertexRange range, VertexRegionMapper vertexRegionMapper) {
        this.range = range;
        this.vertexRegionMapper = vertexRegionMapper;
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

        VertexId newVertexId = node.getVertexId().transformed(id ->
                range.getRight() + 3 * (id - range.getLeft()) + 1);
        RegionId regionId = vertexRegionMapper.getRegionFor(newVertexId);

        Vertex leftChild = aVertex(newVertexId, regionId)
                .withBeggining(node.beginning)
                .withEnding(node.beginning + (node.ending - node.beginning) / 3.0)
                .inMesh(mesh)
                .build();

        node.setLeftChild(weakReferenceToVertex(leftChild));

        processingContext.storeVertex(leftChild);
    }

    private void setMiddleChild(ProcessingContext processingContext) {
        final Vertex node = processingContext.getVertex();
        final Mesh mesh = processingContext.getMesh();

        VertexId newVertexId = node.getVertexId().transformed(id ->
                range.getRight() + 3 * (id - range.getLeft()) + 2);
        RegionId regionId = vertexRegionMapper.getRegionFor(newVertexId);

        Vertex rightChild = aVertex(newVertexId, regionId)
                .withBeggining(node.beginning + (node.ending - node.beginning) / 3.0)
                .withEnding(node.ending - (node.ending - node.beginning) / 3.0)
                .inMesh(mesh)
                .build();

        node.setMiddleChild(weakReferenceToVertex(rightChild));

        processingContext.storeVertex(rightChild);
    }

    private void setRightChild(ProcessingContext processingContext) {
        final Vertex node = processingContext.getVertex();
        final Mesh mesh = processingContext.getMesh();

        VertexId newVertexId = node.getVertexId().transformed(id ->
                range.getRight() + 3 * (id - range.getLeft()) + 3);
        RegionId regionId = vertexRegionMapper.getRegionFor(newVertexId);

        Vertex rightChild = aVertex(newVertexId, regionId)
                .withBeggining(node.beginning + (node.ending - node.beginning) * 2.0 / 3.0)
                .withEnding(node.ending)
                .inMesh(mesh)
                .build();

        node.setRightChild(weakReferenceToVertex(rightChild));

        processingContext.storeVertex(rightChild);
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeObject(range);
        out.writeObject(vertexRegionMapper);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        range = in.readObject();
        vertexRegionMapper = in.readObject();
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