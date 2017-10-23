package com.agh.iet.komplastech.solver.approximation;

import org.junit.Test;

import static java.util.Comparator.comparingDouble;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.newArrayList;

public class KroneckerApproximationManagerTest {

    private final KroneckerApproximator approximator = new KroneckerApproximator();
    private final KroneckerApproximationManager kroneckerApproximationManager = new KroneckerApproximationManager(approximator);

    @Test
    public void canApproximateMultipleIterations() {
        assertThat(kroneckerApproximationManager.approximateInIterations(new double[][]{
                new double[]{1, 2},
                new double[]{3, 4}
        }, 4)).isEqualTo(KroneckerApproximationSeries.builder()
                .series(newArrayList(
                    KroneckerApproximation.builder().build()
                )).build());
    }

    @Test
    public void canApproximateMultipleIterationsForBiggerSystem() {
        assertThat(kroneckerApproximationManager.approximateInIterations(new double[][]{
                new double[]{1, 9, 2},
                new double[]{3, 4, 5},
                new double[]{3, 4, 1}
        }, 4)).isEqualTo(KroneckerApproximationSeries.builder()
                .series(newArrayList(
                        KroneckerApproximation.builder().build()
                )).build());
    }

    @Test
    public void errorBecomesSmallerUpToLimit() {
        assertThat(kroneckerApproximationManager.approximateInIterations(new double[][]{
                new double[]{1, 9, 2},
                new double[]{3, 4, 5},
                new double[]{3, 4, 1}
        }, 3).getSeries())
                .extracting("error")
                .isSortedAccordingTo(comparingDouble(a -> (Double) a).reversed());
    }

}