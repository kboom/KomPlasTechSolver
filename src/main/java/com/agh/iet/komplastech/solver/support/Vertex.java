package com.agh.iet.komplastech.solver.support;

import com.agh.iet.komplastech.solver.VertexId;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Vertex implements Serializable {

    private VertexId id;

    public double[][] m_a;
    public double[][] m_b;

    public double[][] m_x;
    public double beginning;
    public double ending;

    private VertexReference parent;
    private VertexReference leftChild;
    private VertexReference middleChild;
    private VertexReference rightChild;

    public VertexId getId() {
        return id;
    }

    public void setParent(VertexReference parent) {
        this.parent = parent;
    }

    public void setLeftChild(VertexReference leftChild) {
        this.leftChild = leftChild;
    }

    public void setMiddleChild(VertexReference middleChild) {
        this.middleChild = middleChild;
    }

    public void setRightChild(VertexReference rightChild) {
        this.rightChild = rightChild;
    }

    public Vertex getParent() {
        return parent.get();
    }

    public List<Vertex> getChildren() {
        return Stream.of(leftChild.get(), middleChild.get(), rightChild.get())
                .filter(val -> val != null).collect(Collectors.toList());
    }

    public static VertexBuilder aVertex() {
        return new VertexBuilder();
    }

    public Vertex getLeftChild() {
        return leftChild.get();
    }

    public Vertex getRightChild() {
        return rightChild.get();
    }

    public Vertex getMiddleChild() {
        return middleChild.get();
    }

    public static class VertexBuilder {

        private final Vertex vertex;
        private Mesh mesh;

        VertexBuilder() {
            vertex = new Vertex();
        }

        public VertexBuilder withBeggining(double beggining) {
            vertex.beginning = beggining;
            return this;
        }

        public VertexBuilder withEnding(double ending) {
            vertex.ending = ending;
            return this;
        }

        public VertexBuilder inMesh(Mesh mesh) {
            this.mesh = mesh;
            return this;
        }

        public VertexBuilder withId(VertexId id) {
            vertex.id = id;
            return this;
        }

        public Vertex build() {
            vertex.m_a = new double[7][7];
            vertex.m_b = new double[7][mesh.getElementsY() + mesh.getSplineOrder() + 1];
            vertex.m_x = new double[7][mesh.getElementsY() + mesh.getSplineOrder() + 1];
            return vertex;
        }

    }

}