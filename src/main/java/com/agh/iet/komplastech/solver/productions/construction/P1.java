package com.agh.iet.komplastech.solver.productions.construction;

import com.agh.iet.komplastech.solver.VertexId;
import com.agh.iet.komplastech.solver.productions.Production;
import com.agh.iet.komplastech.solver.storage.ObjectStore;
import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.support.Vertex;

import static com.agh.iet.komplastech.solver.VertexId.vertexId;
import static com.agh.iet.komplastech.solver.support.Vertex.aVertex;
import static com.agh.iet.komplastech.solver.support.WeakVertexReference.weakReferenceToVertex;

public class P1 implements Production {

    private static final VertexId LEFT_CHILD_OF_PARENT_ID = vertexId(2);
    private static final VertexId RIGHT_CHILD_OF_PARENT_ID = vertexId(3);

    private final Mesh mesh;
    private final ObjectStore objectStore;

    public P1(ObjectStore objectStore, Mesh mesh) {
        this.objectStore = objectStore;
        this.mesh = mesh;
    }

    public Vertex apply(Vertex node) {
        setLeftChild(node);
        setRightChild(node);
        return node;
    }

    private void setLeftChild(Vertex node) {
        Vertex leftChild = aVertex(LEFT_CHILD_OF_PARENT_ID)
                .withBeggining(0)
                .withEnding(mesh.getElementsX() / 2).build();

        node.setLeftChild(weakReferenceToVertex(leftChild));

        objectStore.storeVertex(leftChild);
    }

    private void setRightChild(Vertex node) {
        Vertex rightChild = aVertex(RIGHT_CHILD_OF_PARENT_ID)
                .withBeggining(mesh.getElementsX() / 2)
                .withEnding(mesh.getElementsX()).build();

        node.setRightChild(weakReferenceToVertex(rightChild));

        objectStore.storeVertex(rightChild);
    }

}
