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
            double naUp = 0, naBt = 0;
            for(int j = 0; j < bCount; j++) {
                naUp += oc[k][j] * nb[j];
                naBt += Math.pow(nb[j], 2);
            }
            na[k] = naUp / naBt;
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

        final double ol[][] = oldApproximation.l;
        final double nl[][] = new double[aCount][bCount];
        for(int i = 0; i < aCount; i++) {
            for (int j = 0; j < bCount; j++) {
                nl[i][j] = ol[i][j] + na[i] * nb[j];
            }
        }

        return KroneckerApproximation.builder()
                .a(na)
                .b(nb)
                .c(nc)
                .l(nl)
                .error(error)
                .build();
    }

}
