package com.agh.iet.komplastech.solver.productions.construction;

import com.agh.iet.komplastech.solver.productions.Production;
import com.agh.iet.komplastech.solver.storage.ObjectStore;
import com.agh.iet.komplastech.solver.support.Vertex;

import java.util.function.LongFunction;

import static com.agh.iet.komplastech.solver.support.Vertex.aVertex;
import static com.agh.iet.komplastech.solver.support.WeakVertexReference.weakReferenceToVertex;

public class P3 implements Production {

    private static final LongFunction<Long> LEFT_CHILD_OF_LEAF = (id) -> 3 * id - 2;
    private static final LongFunction<Long> MIDDLE_CHILD_OF_LEAF = (id) -> 3 * id - 1;
    private static final LongFunction<Long> RIGHT_CHILD_OF_LEAF = (id) -> 3 * id;

    private final ObjectStore objectStore;

    public P3(ObjectStore objectStore) {
        this.objectStore = objectStore;
    }

    public Vertex apply(Vertex node) {
        setLeftChild(node);
        setMiddleChild(node);
        setRightChild(node);
        return node;
    }

    private void setLeftChild(Vertex node) {
        Vertex leftChild = aVertex(node.getId().transformed(LEFT_CHILD_OF_LEAF))
                .withBeggining(node.beginning)
                .withEnding(node.beginning + (node.ending - node.beginning) / 3.0).build();

        node.setLeftChild(weakReferenceToVertex(leftChild));

        objectStore.storeVertex(leftChild);
    }

    private void setMiddleChild(Vertex node) {
        Vertex rightChild = aVertex(node.getId().transformed(MIDDLE_CHILD_OF_LEAF))
                .withBeggining(node.beginning + (node.ending - node.beginning) / 3.0)
                .withEnding(node.ending - (node.ending - node.beginning) / 3.0).build();

        node.setRightChild(weakReferenceToVertex(rightChild));

        objectStore.storeVertex(rightChild);
    }

    private void setRightChild(Vertex node) {
        Vertex rightChild = aVertex(node.getId().transformed(RIGHT_CHILD_OF_LEAF))
                .withBeggining(node.beginning + (node.ending - node.beginning) * 2.0 / 3.0)
                .withEnding(node.ending).build();

        node.setRightChild(weakReferenceToVertex(rightChild));

        objectStore.storeVertex(rightChild);
    }

}