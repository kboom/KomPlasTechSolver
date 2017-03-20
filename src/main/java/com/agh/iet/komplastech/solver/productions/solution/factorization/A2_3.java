package com.agh.iet.komplastech.solver.productions.solution.factorization;

import com.agh.iet.komplastech.solver.productions.ProcessingContext;
import com.agh.iet.komplastech.solver.productions.Production;
import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.support.Vertex;

import static com.agh.iet.komplastech.solver.productions.VertexUtils.swapDofsFor;

public class A2_3 implements Production {

    private final Mesh mesh;

    public A2_3(Mesh mesh) {
        this.mesh = mesh;
    }

    public Vertex apply(ProcessingContext processingContext) {
        System.out.println("A2_3");

        final Vertex currentVertex = processingContext.getVertex();

        System.out.println("Got current vertex");
        System.out.println("Current vertex id " + currentVertex.getId());
        System.out.println("Current vertex children " + currentVertex.getChildren());

        final Vertex leftChild = currentVertex.getLeftChild();

        System.out.println("Got left child");
        System.out.println("Left child " + leftChild.getId());

        System.out.println(String.format("Left child of %s that is %s matrix is %s", currentVertex.getId(), leftChild.getId(), leftChild.getEquation()));

        final Vertex middleChild = currentVertex.getMiddleChild();

        System.out.println(String.format("Middle child of %s that is %s matrix is %s", currentVertex.getId(), middleChild.getId(), middleChild.getEquation()));

        final Vertex rightChild = currentVertex.getRightChild();

        System.out.println(String.format("Right child of %s that is %s matrix is %s", currentVertex.getId(), rightChild.getId(), rightChild.getEquation()));

        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 3; j++) {
                currentVertex.m_a[i][j] += leftChild.m_a[i][j];
                currentVertex.m_a[i + 1][j + 1] += middleChild.m_a[i][j];
                currentVertex.m_a[i + 2][j + 2] += rightChild.m_a[i][j];
            }
            for (int j = 1; j <= mesh.getDofsY(); j++) {
                currentVertex.m_b[i][j] += leftChild.m_b[i][j];
                currentVertex.m_b[i + 1][j] += middleChild.m_b[i][j];
                currentVertex.m_b[i + 2][j] += rightChild.m_b[i][j];
            }
        }
        // bring 3rd degree of freedom to the front
        swapDofsFor(currentVertex, 1, 3, 5, mesh.getDofsY());
        swapDofsFor(currentVertex, 2, 3, 5, mesh.getDofsY());
        return currentVertex;
    }

}