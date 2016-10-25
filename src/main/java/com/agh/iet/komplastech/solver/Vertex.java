package com.agh.iet.komplastech.solver;

public class Vertex {

    public String m_label;
    public Vertex m_left;
    public Vertex m_middle;
    public Vertex m_right;
    public Vertex m_parent;
    public MeshData m_mesh;
    public double[][] m_a;
    public double[][] m_b;
    public double[][] m_c;
    public double[][] m_x;
    public double[][] m_y;
    public double m_beg;
    public double m_end;


    public Vertex(Vertex Left, Vertex Middle, Vertex Right, Vertex Parent, String Lab, double Beg, double End, MeshData Mesh) {
        this.m_left = Left;
        this.m_middle = Middle;
        this.m_right = Right;
        this.m_parent = Parent;
        this.m_label = Lab;
        this.m_mesh = Mesh;
        this.m_beg = Beg;
        this.m_end = End;
        m_a = new double[7][7];
        m_b = new double[7][m_mesh.m_nelemy + m_mesh.m_norder + 1];
        m_c = new double[7][m_mesh.m_nelemx + m_mesh.m_norder + 1];
        m_x = new double[7][m_mesh.m_nelemy + m_mesh.m_norder + 1];
        m_y = new double[7][m_mesh.m_nelemx + m_mesh.m_norder + 1];
    }

    public void set_left(Vertex Left) {
        m_left = Left;
    }

    public void set_middle(Vertex Middle) {
        m_middle = Middle;
    }

    public void set_right(Vertex Right) {
        m_right = Right;
    }

    public void set_parent(Vertex Parent) {
        m_parent = Parent;
    }

    public void set_label(String Lab) {
        m_label = Lab;
    }
}