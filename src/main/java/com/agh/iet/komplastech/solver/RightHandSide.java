package com.agh.iet.komplastech.solver;

import com.agh.iet.komplastech.solver.splines.Function2D;

public class RightHandSide extends Function2D {
    public double get_value(double x, double y) {
//        return Math.sin(2.0*3.1415927535*x)*Math.cos(2.0*3.1415927535*y);
//      return 1; // OK
//      return x;
        return y; // OK
    }
}