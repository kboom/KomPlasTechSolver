package com.agh.iet.komplastech.solver;

public class MeshData {

    // mesh dimension along x
    public final double m_dx;
    // mesh dimension along y
    public final double m_dy;
    // number of elements along x
    public final int m_nelemx;
    // number of elements along y
    public final int m_nelemy;
    // B-spline order
    public final int m_norder;
    // number of dofs along x
    public final int m_dofsx;
    // number of dofs along y
    public final int m_dofsy;

    MeshData(double Dx, double Dy, int Nelemx, int Nelemy, int Norder) {
        m_dx = Dx;
        m_dy = Dy;
        m_nelemx = Nelemx;
        m_nelemy = Nelemy;
        m_norder = Norder;
        m_dofsx = m_nelemx + m_norder;
        m_dofsy = m_nelemy + m_norder;
    }

}
