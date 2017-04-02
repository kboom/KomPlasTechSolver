package com.agh.iet.komplastech.solver.support;

import com.agh.iet.komplastech.solver.VertexId;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Vertex implements DataSerializable {

    private VertexId id;

    public Matrix m_a;
    public Matrix m_b;
    public Matrix m_x;

    public double beginning;
    public double ending;

    private VertexReference leftChild;
    private VertexReference middleChild;
    private VertexReference rightChild;

    @SuppressWarnings("unused")
    public Vertex() {

    }

    public Vertex(VertexId vertexId) {
        id = vertexId;
    }

    public VertexId getId() {
        return id;
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

    public List<Vertex> getChildren() {
        return Stream.of(leftChild, middleChild, rightChild)
                .filter(Objects::nonNull)
                .map(VertexReference::get)
                .collect(Collectors.toList());
    }

    public static VertexBuilder aVertex(VertexId vertexId) {
        return new VertexBuilder(vertexId);
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

    public void visitReferences(ReferenceVisitor referenceVisitor) {
        Stream.of(leftChild, middleChild, rightChild)
                .filter(Objects::nonNull)
                .forEach((vertexReference -> vertexReference.accept(referenceVisitor)));
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeObject(id);
        out.writeObject(m_a);
        out.writeObject(m_b);
        out.writeObject(m_x);
        out.writeDouble(beginning);
        out.writeDouble(ending);
        out.writeObject(leftChild);
        out.writeObject(middleChild);
        out.writeObject(rightChild);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        id = in.readObject();
        m_a = in.readObject();
        m_b = in.readObject();
        m_x = in.readObject();
        beginning = in.readDouble();
        ending = in.readDouble();
        leftChild = in.readObject();
        middleChild = in.readObject();
        rightChild = in.readObject();
    }

    public static class VertexBuilder {

        private final Vertex vertex;
        private Mesh mesh;

        VertexBuilder(VertexId vertexId) {
            vertex = new Vertex(vertexId);
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

        public Vertex build() {
            vertex.m_a = new Matrix(7, 7);
            vertex.m_b = new Matrix(7, mesh.getElementsY() + mesh.getSplineOrder() + 1);
            vertex.m_x = new Matrix(7, mesh.getElementsY() + mesh.getSplineOrder() + 1);
            return vertex;
        }

    }

}