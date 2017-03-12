package com.agh.iet.komplastech.solver.problem;

import java.io.Serializable;

public interface Problem extends Serializable {

    double getValue(double x, double y);

}