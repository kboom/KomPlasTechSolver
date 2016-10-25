package com.agh.iet.komplastech.solver;

class Vertex {
    Vertex(Vertex Left, Vertex Middle, Vertex Right, Vertex Parent, String Lab, double Beg, double End, MeshData Mesh) {
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

    String m_label;
    Vertex m_left;
    Vertex m_middle;
    Vertex m_right;
    Vertex m_parent;
    MeshData m_mesh;
    double[][] m_a;
    double[][] m_b;
    double[][] m_c;
    double[][] m_x;
    double[][] m_y;
    double m_beg;
    double m_end;

    void set_left(Vertex Left) {
        m_left = Left;
    }

    void set_middle(Vertex Middle) {
        m_middle = Middle;
    }

    void set_right(Vertex Right) {
        m_right = Right;
    }

    void set_parent(Vertex Parent) {
        m_parent = Parent;
    }

    void set_label(String Lab) {
        m_label = Lab;
    }
}