package com.agh.iet.komplastech.solver.productions.construction;

import com.agh.iet.komplastech.solver.productions.Production;
import com.agh.iet.komplastech.solver.storage.ObjectStore;
import com.agh.iet.komplastech.solver.support.Vertex;

import java.util.function.IntFunction;

import static com.agh.iet.komplastech.solver.support.Vertex.aVertex;
import static com.agh.iet.komplastech.solver.support.WeakVertexReference.weakReferenceToVertex;

public class P2 implements Production {

    private static final IntFunction<Integer> LEFT_CHILD_OF_INTERMEDIATE = (id) -> 2 * id;
    private static final IntFunction<Integer> RIGHT_CHILD_OF_INTERMEDIATE = (id) -> 2 * id + 1;

    private final ObjectStore objectStore;

    public P2(ObjectStore objectStore) {
        this.objectStore = objectStore;
    }

    public Vertex apply(Vertex node) {
        setLeftChild(node);
        setRightChild(node);
        return node;
    }

    private void setLeftChild(Vertex node) {
        Vertex leftChild = aVertex(node.getId().transformed(LEFT_CHILD_OF_INTERMEDIATE))
                .withBeggining(node.beginning)
                .withEnding(node.beginning + (node.ending - node.beginning) * 0.5).build();

        node.setLeftChild(weakReferenceToVertex(leftChild));

        objectStore.storeVertex(leftChild);
    }

    private void setRightChild(Vertex node) {
        Vertex rightChild = aVertex(node.getId().transformed(RIGHT_CHILD_OF_INTERMEDIATE))
                .withBeggining(node.beginning + (node.ending - node.beginning) * 0.5)
                .withEnding(node.ending).build();

        node.setRightChild(weakReferenceToVertex(rightChild));

        objectStore.storeVertex(rightChild);
    }

}
