package com.agh.iet.komplastech.solver.approximation;

public class KroneckerApproximator {

    public KroneckerApproximation approximate(KroneckerApproximation oldApproximation) {
        final int aCount = oldApproximation.a.length;
        final int bCount = oldApproximation.b.length;

        final double[][] oc = oldApproximation.c;

        final double nb[] = new double [bCount];
        nb[0] = oc[0][0];
        for(int k = 1; k < bCount; k++) {
            final int j = k - 1;
            nb[k] = nb[j] * oc[j][k] / oc[j][j];
        }

        final double na[] = new double [aCount];
        for(int k = 0; k < aCount; k++) {
            na[k] = 0;
            for(int j = 0; j < bCount; j++) {
                na[k] += oc[k][j] / nb[j];
            }
        }

        final double nc[][] = new double[aCount][bCount];

        for(int i = 0; i < aCount; i++) {
            for(int j = 0; j < bCount; j++) {
                nc[i][j] = oldApproximation.c[i][j] - na[i] * nb[j];
            }
        }

        double error = 0;
        for(int i = 0; i < aCount; i++) {
            for(int j = 0; j < bCount; j++) {
                error += Math.pow(nc[i][j] - na[i] * nb[j], 2);
            }
        }

        return KroneckerApproximation.builder()
                .a(na)
                .b(nb)
                .c(nc)
                .error(error)
                .build();
    }

}
