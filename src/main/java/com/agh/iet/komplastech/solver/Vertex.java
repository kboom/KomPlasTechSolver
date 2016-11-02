package com.agh.iet.komplastech.solver;

public class Vertex {

    public Vertex leftChild;
    public Vertex middleChild;
    public Vertex rightChild;
    public Mesh m_mesh;

    public double[][] m_a;
    public double[][] m_b;
    public double[][] m_x;

    public double m_beg;
    public double m_end;

    private Vertex() {
    }

    public void setLeftChild(Vertex leftChild) {
        this.leftChild = leftChild;
    }

    public void setMiddleChild(Vertex middleChild) {
        this.middleChild = middleChild;
    }

    public void setRightChild(Vertex rightChild) {
        this.rightChild = rightChild;
    }

    public static VertexBuilder aVertex() {
        return new VertexBuilder();
    }

    public static class VertexBuilder {

        private Vertex vertex = new Vertex();

        public VertexBuilder withLeftChild(Vertex leftChild) {
            vertex.leftChild = leftChild;
            return this;
        }

        public VertexBuilder withMiddleChild(Vertex middleChild) {
            vertex.middleChild = middleChild;
            return this;
        }


        public VertexBuilder withRightChild(Vertex rightChild) {
            vertex.rightChild = rightChild;
            return this;
        }

        public VertexBuilder withBeggining(double beggining) {
            vertex.m_beg = beggining;
            return this;
        }

        public VertexBuilder withEnding(double ending) {
            vertex.m_end = ending;
            return this;
        }

        public VertexBuilder withMesh(Mesh mesh) {
            vertex.m_mesh = mesh;
            return this;
        }

        public Vertex build() {
            vertex.m_a = new double[7][7];
            vertex.m_b = new double[7][vertex.m_mesh.getElementsY() + vertex.m_mesh.getSplineOrder() + 1];
            vertex.m_x = new double[7][vertex.m_mesh.getElementsY() + vertex.m_mesh.getSplineOrder() + 1];
            return vertex;
        }

    }

}