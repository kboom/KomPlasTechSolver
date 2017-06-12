package com.agh.iet.komplastech.solver.productions.construction;

import com.agh.iet.komplastech.solver.productions.LocalProcessingContext;
import com.agh.iet.komplastech.solver.productions.ProcessingContext;
import com.agh.iet.komplastech.solver.storage.InMemoryObjectStore;
import com.agh.iet.komplastech.solver.storage.ObjectStore;
import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.support.Vertex;
import org.junit.Test;

import static com.agh.iet.komplastech.solver.VertexId.vertexId;
import static com.agh.iet.komplastech.solver.support.Mesh.aMesh;
import static com.agh.iet.komplastech.solver.support.RegionId.regionId;
import static com.agh.iet.komplastech.solver.support.Vertex.aVertex;
import static org.assertj.core.api.Assertions.assertThat;

public class P1Test {

    private static final int GRID_SIZE = 12;

    private Mesh DUMMY_MESH = aMesh()
            .withElementsX(GRID_SIZE)
            .withElementsY(GRID_SIZE)
            .withResolutionX(12d)
            .withResolutionY(12d)
            .withOrder(2).build();


    @Test
    public void attachesLeftChild() {
        Vertex root = createRoot();
        P1 p1 = new P1();
        p1.apply(new LocalProcessingContext(root));
        assertThat(root.getLeftChild()).isNotNull();
    }

    @Test
    public void leftBoundaryOfLeftChildIsZero() {
        Vertex root = createRoot();
        P1 p1 = new P1();
        p1.apply(new LocalProcessingContext(root));
        assertThat(root.getLeftChild().beginning).isZero();
    }

    @Test
    public void rightBoundaryOfLeftChildIsHalfTheSizeOfGrid() {
        Vertex root = createRoot();
        P1 p1 = new P1();
        p1.apply(new LocalProcessingContext(root));
        assertThat(root.getLeftChild().ending).isEqualTo(GRID_SIZE / 2);
    }

    @Test
    public void attachesRightChild() {
        Vertex root = createRoot();
        P1 p1 = new P1();
        p1.apply(new LocalProcessingContext(root));
        assertThat(root.getRightChild()).isNotNull();
    }

    @Test
    public void leftBoundaryOfRightChildIsHalfTheSizeOfGrid() {
        Vertex root = createRoot();
        P1 p1 = new P1();
        p1.apply(new LocalProcessingContext(root));
        assertThat(root.getRightChild().beginning).isEqualTo(GRID_SIZE / 2);
    }

    @Test
    public void rightBoundaryOfLeftChildIsTheGridSize() {
        Vertex root = createRoot();
        P1 p1 = new P1();
        p1.apply(new LocalProcessingContext(root));
        assertThat(root.getRightChild().ending).isEqualTo(GRID_SIZE);
    }

    private Vertex createRoot() {
        return aVertex(vertexId(1), regionId(1)).inMesh(DUMMY_MESH).build();
    }

}