package com.agh.iet.komplastech.solver.productions.construction;

import com.agh.iet.komplastech.solver.productions.Production;
import com.agh.iet.komplastech.solver.storage.ObjectStore;
import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.support.Vertex;
import com.agh.iet.komplastech.solver.support.VertexReference;

import static com.agh.iet.komplastech.solver.support.Vertex.aVertex;
import static com.agh.iet.komplastech.solver.support.WeakVertexReference.weakReferenceToVertex;

public class P1 extends Production {

    private ObjectStore objectStore;

    public P1(ObjectStore objectStore, Mesh mesh) {
        super(mesh);
        this.objectStore = objectStore;
    }

    public Vertex apply(Vertex node) {
        setLeftChild(node);
        setRightChild(node);
        return node;
    }

    private void setLeftChild(Vertex node) {
        Vertex leftChild = objectStore.createNewVertex(
                aVertex()
                        .withBeggining(0)
                        .withEnding(mesh.getElementsX() / 2),
                newNode -> newNode.setParent(referenceFromFor(newNode, node))
        );
        node.setLeftChild(referenceFromFor(node, leftChild));

    }

    private void setRightChild(Vertex node) {
        Vertex rightChild = objectStore.createNewVertex(
                aVertex()
                        .withBeggining(mesh.getElementsX() / 2)
                        .withEnding(mesh.getElementsX()),
                newNode -> newNode.setParent(referenceFromFor(newNode, node))
        );
        node.setRightChild(referenceFromFor(node, rightChild));
    }

    /**
     * For now create Weak References for every relation in the tree.
     * <p>
     * Later use node clustering - parents should have references to their closest children to make production operation real atomic...
     * Consider what is needed to do it (if operation is read only then do not store updated value).
     *
     * <p>
     * todo: Use strong referenceFromFor if the cluster of nodes didn't reach maximum size yet or use weak referenceFromFor if it did.
     */
    private VertexReference referenceFromFor(Vertex from, Vertex to) {
        return weakReferenceToVertex(to);
    }

}
