package com.agh.iet.komplastech.solver;

public class Vertex {

    public Vertex m_left;
    public Vertex m_middle;
    public Vertex m_right;
    public Mesh m_mesh;

    public final double[][] m_a;
    public final double[][] m_b;
    public final double[][] m_x;

    public double m_beg;
    public double m_end;

    public Vertex(Vertex Left, Vertex Middle, Vertex Right, double Beg, double End, Mesh Mesh) {
        this.m_left = Left;
        this.m_middle = Middle;
        this.m_right = Right;
        this.m_mesh = Mesh;
        this.m_beg = Beg;
        this.m_end = End;
        m_a = new double[7][7];
        m_b = new double[7][m_mesh.getElementsY() + m_mesh.getSplineOrder() + 1];
        m_x = new double[7][m_mesh.getElementsY() + m_mesh.getSplineOrder() + 1];
    }

    public void setLeftChild(Vertex leftChild) {
        m_left = leftChild;
    }

    public void setMiddleChild(Vertex middleChild) {
        m_middle = middleChild;
    }

    public void setRightChild(Vertex rightChild) {
        m_right = rightChild;
    }

}