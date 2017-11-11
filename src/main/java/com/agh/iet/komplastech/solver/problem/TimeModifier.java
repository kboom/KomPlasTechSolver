package com.agh.iet.komplastech.solver.problem;

@FunctionalInterface
public interface TimeModifier {

    double getAddedValue(double x, double y, double time);

}
