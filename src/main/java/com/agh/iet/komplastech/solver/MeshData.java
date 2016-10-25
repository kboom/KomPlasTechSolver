package com.agh.iet.komplastech.solver;

class MeshData {
    MeshData(double Dx, double Dy, int Nelemx, int Nelemy, int Norder) {
        m_dx = Dx;
        m_dy = Dy;
        m_nelemx = Nelemx;
        m_nelemy = Nelemy;
        m_norder = Norder;
        m_dofsx = m_nelemx + m_norder;
        m_dofsy = m_nelemy + m_norder;
    }

    // mesh dimension along x
    double m_dx;
    // mesh dimension along y
    double m_dy;
    // number of elements along x
    int m_nelemx;
    // number of elements along y
    int m_nelemy;
    // B-spline order
    int m_norder;
    // number of dofs along x
    int m_dofsx;
    // number of dofs along y
    int m_dofsy;
}
