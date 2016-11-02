package com.agh.iet.komplastech.solver.splines;

public class Bspline1 extends Spline {

    public Bspline1() {
        super(0, 1);
    }

    protected double getFunctionValue(double x) {
        return 0.5 * x * x;
    }

}
