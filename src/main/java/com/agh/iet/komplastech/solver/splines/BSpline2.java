package com.agh.iet.komplastech.solver.splines;

public class BSpline2 extends Spline {

    public BSpline2() {
        super(0, 1);
    }

    protected double getFunctionValue(double x) {
        return (-2 * (x + 1) * (x + 1) + 6 * (x + 1) - 3) * 0.5;
    }

    @Override
    public double getSecondDerivativeValueAt(double x) {
        return x < 0 || x > 1 ? 0 : -2.0;
    }

    @Override
    public double getFirstDerivativeValueAt(double x) {
        return x < 0 || x > 1 ? 0 : 1 - 2 * x;
    }

}
