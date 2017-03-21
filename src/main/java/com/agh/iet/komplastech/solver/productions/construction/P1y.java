package com.agh.iet.komplastech.solver.productions.construction;

import com.agh.iet.komplastech.solver.VertexId;
import com.agh.iet.komplastech.solver.productions.ProcessingContext;
import com.agh.iet.komplastech.solver.productions.Production;
import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.support.Vertex;

import static com.agh.iet.komplastech.solver.VertexId.vertexId;
import static com.agh.iet.komplastech.solver.support.Vertex.aVertex;
import static com.agh.iet.komplastech.solver.support.WeakVertexReference.weakReferenceToVertex;

public class P1y implements Production {

    private static final VertexId LEFT_CHILD_OF_PARENT_ID = vertexId(2);
    private static final VertexId RIGHT_CHILD_OF_PARENT_ID = vertexId(3);

    private final Mesh mesh;

    public P1y(Mesh mesh) {
        this.mesh = mesh;
    }

    public void apply(ProcessingContext processingContext) {
        setLeftChild(processingContext);
        setRightChild(processingContext);
        processingContext.updateVertex();
    }

    private void setLeftChild(ProcessingContext processingContext) {
        Vertex node = processingContext.getVertex();

        Vertex leftChild = aVertex(LEFT_CHILD_OF_PARENT_ID)
                .withBeggining(0)
                .withEnding(mesh.getElementsY() / 2.0)
                .inMesh(mesh)
                .build();

        node.setLeftChild(weakReferenceToVertex(leftChild));

        processingContext.storeVertex(leftChild);
    }

    private void setRightChild(ProcessingContext processingContext) {
        Vertex node = processingContext.getVertex();

        Vertex rightChild = aVertex(RIGHT_CHILD_OF_PARENT_ID)
                .withBeggining(mesh.getElementsY() / 2.0)
                .withEnding(mesh.getElementsY())
                .inMesh(mesh)
                .build();

        node.setRightChild(weakReferenceToVertex(rightChild));

        processingContext.storeVertex(rightChild);
    }

}
