package com.agh.iet.komplastech.solver.approximation;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Builder
@EqualsAndHashCode
@ToString
class KroneckerApproximation {

    public final double[][] c;

    public final double a[];

    public final double b[];

    public final double error;

    public static KroneckerApproximation originalOnly(double[][] original) {
        return KroneckerApproximation.builder()
                .a(new double[original.length])
                .b(new double[original.length])
                .c(original)
                .build();
    }

}
