package com.agh.iet.komplastech.solver;

class RightHandSide extends Function2D {
    double get_value(double x, double y) {
//        return Math.sin(2.0*3.1415927535*x)*Math.cos(2.0*3.1415927535*y);
//      return 1; // OK
//      return x;
        return y; // OK
    }
}