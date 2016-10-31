package com.agh.iet.komplastech.solver;

import com.agh.iet.komplastech.solver.splines.Function2D;

public class RightHandSide extends Function2D {
    public double get_value(double x, double y) {
        return x + y;
    }
}