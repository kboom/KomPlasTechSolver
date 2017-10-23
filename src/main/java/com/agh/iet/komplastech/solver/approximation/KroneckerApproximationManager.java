package com.agh.iet.komplastech.solver.approximation;

import lombok.AllArgsConstructor;

import java.util.Collections;
import java.util.LinkedList;

@AllArgsConstructor
public class KroneckerApproximationManager {

    private final KroneckerApproximator approximator;

    public KroneckerApproximationSeries approximateInIterations(double[][] original, int iterations) {
        LinkedList<KroneckerApproximation> kroneckerApproximations = new LinkedList<>();
        KroneckerApproximation initialApproximation = approximator.approximate(
                KroneckerApproximation.originalOnly(original)
        );
        kroneckerApproximations.push(initialApproximation);

        for(int i = 1; i < iterations; i++) {
            kroneckerApproximations.push(approximator.approximate(kroneckerApproximations.peek()));
        }

        Collections.reverse(kroneckerApproximations);

        return KroneckerApproximationSeries.builder()
                .series(kroneckerApproximations)
                .build();
    }

}
