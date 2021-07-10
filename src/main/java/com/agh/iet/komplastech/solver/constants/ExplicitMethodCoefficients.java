package com.agh.iet.komplastech.solver.constants;

import com.agh.iet.komplastech.solver.support.Vertex;

public final class ExplicitMethodCoefficients implements MethodCoefficients {

    public static final MethodCoefficients EXPLICIT_METHOD_COEFFICIENTS = new ExplicitMethodCoefficients();

    private ExplicitMethodCoefficients() {
    }

    @Override
    public void bindMethodCoefficients(Vertex node) {
        node.m_a.set(1, 1, 1.0 / 20.0);
        node.m_a.set(1, 2, 13.0 / 120);
        node.m_a.set(1, 3, 1.0 / 120);
        node.m_a.set(2, 1, 13.0 / 120.0);
        node.m_a.set(2, 2, 45.0 / 100.0);
        node.m_a.set(2, 3, 13.0 / 120.0);
        node.m_a.set(3, 1, 1.0 / 120.0);
        node.m_a.set(3, 2, 13.0 / 120.0);
        node.m_a.set(3, 3, 1.0 / 20.0);
    }

}
