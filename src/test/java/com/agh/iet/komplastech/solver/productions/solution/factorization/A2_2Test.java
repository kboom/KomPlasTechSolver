package com.agh.iet.komplastech.solver.productions.solution.factorization;

import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.support.Vertex;
import org.junit.Test;

import static com.agh.iet.komplastech.solver.support.Mesh.aMesh;
import static com.agh.iet.komplastech.solver.support.Vertex.aVertex;
import static java.util.Arrays.deepToString;
import static org.assertj.core.api.Assertions.assertThat;

public class A2_2Test {

    private static final int GRID_SIZE = 12;

    private Mesh DUMMY_MESH = aMesh()
            .withElementsX(GRID_SIZE)
            .withElementsY(GRID_SIZE)
            .withResolutionX(12d)
            .withResolutionY(12d)
            .withOrder(2).build();

    @Test
    public void canMerge() {
        Vertex parent = createParent();
        Vertex rightChild = createRightChild(parent);
        Vertex leftChild = createLeftChild(parent);

        leftChild.m_a[2][2] = 1;
        rightChild.m_a[2][2] = 1;

        A2_2 a = new A2_2(parent, DUMMY_MESH);
        a.apply(parent);
        assertThat(deepToString(parent.m_a)).isEqualTo(
                "[[0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0], " +
                "[0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0], " +
                "[0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0], " +
                "[0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0], " +
                "[0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0], " +
                "[0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0], " +
                "[0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0]]"
        );
    }

    @Test
    public void swapsRHS() {
        Vertex parent = createParent();
        Vertex rightChild = createRightChild(parent);
        Vertex leftChild = createLeftChild(parent);

        leftChild.m_b[3][3] = 1;
        rightChild.m_b[3][3] = 1;

        A2_2 a = new A2_2(parent, DUMMY_MESH);
        a.apply(parent);
        assertThat(deepToString(parent.m_b)).isEqualTo(
                "[[0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0], " +
                "[0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0], " +
                "[0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0], " +
                "[0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0], " +
                "[0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0], " +
                "[0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0], " +
                "[0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0]]"
        );
    }

    private Vertex createParent() {
        return aVertex().withMesh(DUMMY_MESH).build();
    }

    private Vertex createLeftChild(Vertex node) {
        Vertex leftChild = aVertex().withMesh(DUMMY_MESH).build();
        node.setLeftChild(leftChild);
        return leftChild;
    }

    private Vertex createRightChild(Vertex node) {
        Vertex rightChild = aVertex().withMesh(DUMMY_MESH).build();
        node.setRightChild(rightChild);
        return rightChild;
    }

}