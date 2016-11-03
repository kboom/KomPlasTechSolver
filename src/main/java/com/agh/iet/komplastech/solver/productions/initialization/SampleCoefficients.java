package com.agh.iet.komplastech.solver.productions.initialization;

import com.agh.iet.komplastech.solver.support.Vertex;

class SampleCoefficients {

    static void useArbitraryCoefficients(Vertex node) {
        node.m_a[1][1] = 1.0 / 20.0;
        node.m_a[1][2] = 13.0 / 120;
        node.m_a[1][3] = 1.0 / 120;
        node.m_a[2][1] = 13.0 / 120.0;
        node.m_a[2][2] = 45.0 / 100.0;
        node.m_a[2][3] = 13.0 / 120.0;
        node.m_a[3][1] = 1.0 / 120.0;
        node.m_a[3][2] = 13.0 / 120.0;
        node.m_a[3][3] = 1.0 / 20.0;
    }

}
