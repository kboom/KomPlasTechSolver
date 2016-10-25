package com.agh.iet.komplastech.solver.splines;

public class Bspline1 extends Function1D {
    public double get_value(double x) {
        if (x < 0 || x > 1)
            return 0;
        else
            return 0.5 * x * x;
    }
}
