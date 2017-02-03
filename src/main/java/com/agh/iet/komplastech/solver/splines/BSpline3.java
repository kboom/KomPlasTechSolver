package com.agh.iet.komplastech.solver.splines;

public class BSpline3 extends Spline {

    public BSpline3() {
        super(0, 1);
    }

    protected double getFunctionValue(double x) {
        return 0.5 * (1 - x) * (1 - x);
    }

    @Override
    public double getSecondDerivativeValueAt(double x) {
        return x < 0 || x > 1 ? 0 : 1.0;
    }

}
