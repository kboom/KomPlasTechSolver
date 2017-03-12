package com.agh.iet.komplastech.solver.productions.construction;

import com.agh.iet.komplastech.solver.productions.ProcessingContext;
import com.agh.iet.komplastech.solver.productions.Production;
import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.support.Vertex;

import java.util.function.IntFunction;

import static com.agh.iet.komplastech.solver.support.Vertex.aVertex;
import static com.agh.iet.komplastech.solver.support.WeakVertexReference.weakReferenceToVertex;

public class P3 implements Production {

    private static final IntFunction<Integer> LEFT_CHILD_OF_LEAF = (id) -> 3 * id - 2;
    private static final IntFunction<Integer> MIDDLE_CHILD_OF_LEAF = (id) -> 3 * id - 1;
    private static final IntFunction<Integer> RIGHT_CHILD_OF_LEAF = (id) -> 3 * id;

    private Mesh mesh;

    public P3(Mesh mesh) {
        this.mesh = mesh;
    }

    public Vertex apply(ProcessingContext processingContext) {
        setLeftChild(processingContext);
        setMiddleChild(processingContext);
        setRightChild(processingContext);
        return processingContext.getVertex();
    }

    private void setLeftChild(ProcessingContext processingContext) {
        Vertex node = processingContext.getVertex();

        Vertex leftChild = aVertex(node.getId().transformed(LEFT_CHILD_OF_LEAF))
                .withBeggining(node.beginning)
                .withEnding(node.beginning + (node.ending - node.beginning) / 3.0)
                .inMesh(mesh)
                .build();

        node.setLeftChild(weakReferenceToVertex(leftChild));

        processingContext.storeVertex(leftChild);
    }

    private void setMiddleChild(ProcessingContext processingContext) {
        Vertex node = processingContext.getVertex();

        Vertex rightChild = aVertex(node.getId().transformed(MIDDLE_CHILD_OF_LEAF))
                .withBeggining(node.beginning + (node.ending - node.beginning) / 3.0)
                .withEnding(node.ending - (node.ending - node.beginning) / 3.0)
                .inMesh(mesh)
                .build();

        node.setRightChild(weakReferenceToVertex(rightChild));

        processingContext.storeVertex(rightChild);
    }

    private void setRightChild(ProcessingContext processingContext) {
        Vertex node = processingContext.getVertex();

        Vertex rightChild = aVertex(node.getId().transformed(RIGHT_CHILD_OF_LEAF))
                .withBeggining(node.beginning + (node.ending - node.beginning) * 2.0 / 3.0)
                .withEnding(node.ending)
                .inMesh(mesh)
                .build();

        node.setRightChild(weakReferenceToVertex(rightChild));

        processingContext.storeVertex(rightChild);
    }

}