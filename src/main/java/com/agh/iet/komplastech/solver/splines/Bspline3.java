package com.agh.iet.komplastech.solver.splines;

public class Bspline3 extends Spline {

    private static final int VALUE_FOR_UNDEFINED = 0;

    public Bspline3() {
        super(0, 1);
    }

    protected double getFunctionValue(double x) {
        return 0.5 * (1 - x) * (1 - x);
    }

}
