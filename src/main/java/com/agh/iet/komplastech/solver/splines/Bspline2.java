package com.agh.iet.komplastech.solver.splines;

public class Bspline2 extends Function1D {
    public double get_value(double x) {
        if (x < 0 || x > 1)
            return 0;
        else
            return (-2 * (x + 1) * (x + 1) + 6 * (x + 1) - 3) * 0.5;
    }
}
