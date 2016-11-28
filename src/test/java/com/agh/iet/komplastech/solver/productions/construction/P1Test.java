package com.agh.iet.komplastech.solver.productions.construction;

import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.support.Vertex;
import org.junit.Test;

import static com.agh.iet.komplastech.solver.support.Mesh.aMesh;
import static com.agh.iet.komplastech.solver.support.Vertex.aVertex;
import static org.assertj.core.api.Assertions.assertThat;

public class P1Test {

    private Mesh DUMMY_MESH = aMesh()
            .withElementsX(12)
            .withElementsY(12)
            .withResolutionX(12d)
            .withResolutionY(12d)
            .withOrder(2).build();


    @Test
    public void attachesLeftChild() {
        Vertex root = aVertex().withMesh(DUMMY_MESH).build();
        P1 p1 = new P1(root, DUMMY_MESH);
        p1.apply(root);
        assertThat(root.leftChild).isNotNull();
    }

    @Test
    public void attachesRightChild() {
        Vertex root = aVertex().withMesh(DUMMY_MESH).build();
        P1 p1 = new P1(root, DUMMY_MESH);
        p1.apply(root);
        assertThat(root.rightChild).isNotNull();
    }


}