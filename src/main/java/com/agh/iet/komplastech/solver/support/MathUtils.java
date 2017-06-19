package com.agh.iet.komplastech.solver.support;

public abstract class MathUtils {

    public static int log2(double value) {
        return (int) Math.floor(Math.log(value) / Math.log(2));
    }
}