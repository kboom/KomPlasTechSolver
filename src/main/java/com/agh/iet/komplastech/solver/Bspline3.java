package com.agh.iet.komplastech.solver;

class Bspline3 extends Function1D {
    double get_value(double x) {
        if (x < 0 || x > 1)
            return 0;
        else
            return 0.5 * (1 - x) * (1 - x);
    }
}
