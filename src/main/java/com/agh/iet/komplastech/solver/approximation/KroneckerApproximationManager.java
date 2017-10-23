package com.agh.iet.komplastech.solver.approximation;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class KroneckerApproximationManager {

    private final KroneckerApproximator approximator;

    public KroneckerApproximation approximateInIterations(double[][] original, int iterations) {
        KroneckerApproximation currentApproximation = approximator.approximate(
                KroneckerApproximation.originalOnly(original)
        );

        for(int i = 1; i < iterations; i++) {
            currentApproximation = approximator.approximate(currentApproximation);
        }

        return currentApproximation;
    }

}
