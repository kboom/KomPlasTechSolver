package com.agh.iet.komplastech.solver.approximation;

import org.junit.Test;

import static java.util.Comparator.comparingDouble;
import static org.assertj.core.api.Assertions.assertThat;

public class KroneckerApproximationManagerTest {

    private final KroneckerApproximator approximator = new KroneckerApproximator();
    private final KroneckerApproximationManager kroneckerApproximationManager = new KroneckerApproximationManager(approximator);

    @Test
    public void canApproximateOnce() {
        assertThat(kroneckerApproximationManager.approximateInIterations(new double[][]{
                new double[]{1, 9, 2},
                new double[]{3, 4, 5},
                new double[]{3, 4, 1}
        }, 1).getSeries()).containsExactly(
                KroneckerApproximation.builder()
                        .a(new double[]{0.501048846269104, 0.45669763260413543, 0.24093497153131554})
                        .b(new double[]{1.0, 9.0, 11.25})
                        .l(new double[][]{
                                new double[]{0.501048846269104, 4.509439616421935, 5.636799520527419},
                                new double[]{0.45669763260413543, 4.110278693437219, 5.137848366796524},
                                new double[]{0.24093497153131554, 2.16841474378184, 2.7105184297273},
                        })
                        .c(new double[][]{
                                new double[]{0.49895115373089605, 4.490560383578065, -3.6367995205274193},
                                new double[]{2.5433023673958646, -0.11027869343721886, -0.1378483667965238},
                                new double[]{2.7590650284686844, 1.83158525621816, -1.7105184297273}
                        })
                        .error(162)
                        .build()
        );
    }

    @Test
    public void approximatesTarget() {
        assertThat(kroneckerApproximationManager.approximateInIterations(new double[][]{
                new double[]{1, 9, 2, 6, 19},
                new double[]{4, 4, 5, 10, 12},
                new double[]{3, 4, 6, 12, 13},
                new double[]{6, 7, 4, 8, 14},
                new double[]{4, 3, 2, 1, 10}
        }, 1).getTarget().l).isEqualTo(
                new double[][]{
                        new double[]{0.4359984272933581, 3.923985845640223, 4.904982307050278, 9.809964614100556, 17.167438074675974},
                        new double[]{0.35041007635904614, 3.1536906872314154, 3.942113359039269, 7.884226718078538, 13.797396756637442},
                        new double[]{0.3921833720761797, 3.5296503486856174, 4.412062935857022, 8.824125871714044, 15.442220275499576},
                        new double[]{0.37314534430549134, 3.358308098749422, 4.1978851234367776, 8.395770246873555, 14.692597932028722},
                        new double[]{0.20737654597752686, 1.8663889137977416, 2.332986142247177, 4.665972284494354, 8.16545149786512}
                }
        );
    }

    @Test
    public void errorBecomesSmaller() {
        assertThat(kroneckerApproximationManager.approximateInIterations(new double[][]{
                new double[]{1, 9, 2, 6},
                new double[]{3, 4, 5, 10},
                new double[]{3, 4, 1, 12},
                new double[]{19, 6, 3, 12}
        }, 100).getSeries())
                .extracting("error")
                .isSortedAccordingTo(comparingDouble(a -> (Double) a).reversed());
    }

}