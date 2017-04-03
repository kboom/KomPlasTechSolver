package com.agh.iet.komplastech.solver.productions.construction;

import com.agh.iet.komplastech.solver.VertexId;
import com.agh.iet.komplastech.solver.productions.ProcessingContext;
import com.agh.iet.komplastech.solver.productions.Production;
import com.agh.iet.komplastech.solver.support.*;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;

import java.io.IOException;
import java.util.function.IntFunction;

import static com.agh.iet.komplastech.solver.factories.HazelcastProductionFactory.P2_PRODUCTION;
import static com.agh.iet.komplastech.solver.factories.HazelcastProductionFactory.PRODUCTION_FACTORY;
import static com.agh.iet.komplastech.solver.support.RegionId.regionId;
import static com.agh.iet.komplastech.solver.support.Vertex.aVertex;
import static com.agh.iet.komplastech.solver.support.WeakVertexReference.weakReferenceToVertex;

/**
 * Child nodes may either:
 * <ul>
 * <li><b>Belong to the same region as parents</b> - if they are between region boundaries</li>
 * <li><b>Form a new region</b> - if they are on a region boundary</li>
 * </ul>
 */
public class P2 implements Production {

    private static final IntFunction<Integer> LEFT_CHILD_OF_INTERMEDIATE = (id) -> 2 * id;
    private static final IntFunction<Integer> RIGHT_CHILD_OF_INTERMEDIATE = (id) -> 2 * id + 1;

    private Mesh mesh;
    private VertexRegionMapper vertexRegionMapper;

    @SuppressWarnings("unused")
    public P2() {

    }

    public P2(Mesh mesh, VertexRegionMapper vertexRegionMapper) {
        this.mesh = mesh;
        this.vertexRegionMapper = vertexRegionMapper;
    }

    public void apply(ProcessingContext processingContext) {
        setLeftChild(processingContext);
        setRightChild(processingContext);
        processingContext.updateVertex();
    }

    private void setLeftChild(ProcessingContext processingContext) {
        Vertex node = processingContext.getVertex();

        VertexId newVertexId = node.getVertexId().transformed(LEFT_CHILD_OF_INTERMEDIATE);
        RegionId regionId = vertexRegionMapper.getRegionFor(newVertexId);

        Vertex leftChild = aVertex(newVertexId, regionId)
                .withBeggining(node.beginning)
                .withEnding(node.beginning + (node.ending - node.beginning) * 0.5)
                .inMesh(mesh)
                .build();

        node.setLeftChild(weakReferenceToVertex(leftChild)); // set parent

        processingContext.storeVertex(leftChild);
    }

    private void setRightChild(ProcessingContext processingContext) {
        Vertex node = processingContext.getVertex();

        VertexId newVertexId = node.getVertexId().transformed(RIGHT_CHILD_OF_INTERMEDIATE);
        RegionId regionId = vertexRegionMapper.getRegionFor(newVertexId);

        Vertex rightChild = aVertex(newVertexId, regionId)
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
        out.writeObject(vertexRegionMapper);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        mesh = in.readObject();
        vertexRegionMapper = in.readObject();
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
