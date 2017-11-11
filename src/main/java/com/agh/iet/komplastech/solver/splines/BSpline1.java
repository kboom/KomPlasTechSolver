package com.agh.iet.komplastech.solver.splines;

public class BSpline1 extends Spline {

    public BSpline1() {
        super(0, 1);
    }

    protected double getFunctionValue(double x) {
        return 0.5 * x * x;
    }

    @Override
    public double getSecondDerivativeValueAt(double x) {
        return x < 0 || x > 1 ? 0 : 1.0;
    }

    @Override
    public double getFirstDerivativeValueAt(double x) {
        return x < 0 || x > 1 ? 0 : x;
    }

}
