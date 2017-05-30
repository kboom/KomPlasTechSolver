package com.agh.iet.komplastech.solver.productions.construction;

import com.agh.iet.komplastech.solver.VertexId;
import com.agh.iet.komplastech.solver.productions.ProcessingContext;
import com.agh.iet.komplastech.solver.productions.Production;
import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.support.Vertex;
import com.agh.iet.komplastech.solver.support.VertexRegionMapper;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;

import java.io.IOException;

import static com.agh.iet.komplastech.solver.VertexId.vertexId;
import static com.agh.iet.komplastech.solver.factories.HazelcastProductionFactory.P1y_PRODUCTION;
import static com.agh.iet.komplastech.solver.factories.HazelcastProductionFactory.PRODUCTION_FACTORY;
import static com.agh.iet.komplastech.solver.support.Vertex.aVertex;
import static com.agh.iet.komplastech.solver.support.WeakVertexReference.weakReferenceToVertex;

/**
 * Child nodes belong to the same region.
 */
public class P1y implements Production {

    private static final VertexId LEFT_CHILD_OF_PARENT_ID = vertexId(2);
    private static final VertexId RIGHT_CHILD_OF_PARENT_ID = vertexId(3);

    private VertexRegionMapper vertexRegionMapper;

    @SuppressWarnings("unused")
    public P1y() {

    }

    public P1y(VertexRegionMapper vertexRegionMapper) {
        this.vertexRegionMapper = vertexRegionMapper;
    }

    public void apply(ProcessingContext processingContext) {
        setLeftChild(processingContext);
        setRightChild(processingContext);
        processingContext.updateVertex();
    }

    private void setLeftChild(ProcessingContext processingContext) {
        final Vertex node = processingContext.getVertex();
        final Mesh mesh = processingContext.getMesh();

        Vertex leftChild = aVertex(LEFT_CHILD_OF_PARENT_ID, vertexRegionMapper.getRegionFor(LEFT_CHILD_OF_PARENT_ID))
                .withBeggining(0)
                .withEnding(mesh.getElementsY() / 2.0)
                .inMesh(mesh)
                .build();

        node.setLeftChild(weakReferenceToVertex(leftChild));

        processingContext.storeVertex(leftChild);
    }

    private void setRightChild(ProcessingContext processingContext) {
        final Vertex node = processingContext.getVertex();
        final Mesh mesh = processingContext.getMesh();

        Vertex rightChild = aVertex(RIGHT_CHILD_OF_PARENT_ID, vertexRegionMapper.getRegionFor(RIGHT_CHILD_OF_PARENT_ID))
                .withBeggining(mesh.getElementsY() / 2.0)
                .withEnding(mesh.getElementsY())
                .inMesh(mesh)
                .build();

        node.setRightChild(weakReferenceToVertex(rightChild));

        processingContext.storeVertex(rightChild);
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeObject(vertexRegionMapper);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        vertexRegionMapper = in.readObject();
    }

    @Override
    public int getFactoryId() {
        return PRODUCTION_FACTORY;
    }

    @Override
    public int getId() {
        return P1y_PRODUCTION;
    }

}
