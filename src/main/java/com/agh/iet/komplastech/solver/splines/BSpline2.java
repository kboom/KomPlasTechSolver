package com.agh.iet.komplastech.solver.splines;

public class BSpline2 extends Spline {

    public BSpline2() {
        super(0, 1);
    }

    protected double getFunctionValue(double x) {
        return (-2 * (x + 1) * (x + 1) + 6 * (x + 1) - 3) * 0.5;
    }

}
