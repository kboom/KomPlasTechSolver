package com.agh.iet.komplastech.solver.support;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MatrixPaddingTranslatorTest {

    @Test
    public void canIntroducePadding() {
        assertThat(MatrixPaddingTranslator.withPadding(new double[][] {
                new double[] { 1, 2 },
                new double[] { 3, 4 }
        })).isEqualTo(
                new double[][] {
                        new double[] { 0, 0, 0 },
                        new double[] { 0, 1, 2 },
                        new double[] { 0, 3, 4 }
                }
        );
    }

    @Test
    public void canRemovePadding() {
        assertThat(MatrixPaddingTranslator.withoutPadding(new double[][] {
                new double[] { 0, 0, 0 },
                new double[] { 0, 1, 2 },
                new double[] { 0, 3, 4 }
        })).isEqualTo(
                new double[][] {
                        new double[] { 1, 2 },
                        new double[] { 3, 4 }
                }
        );
    }

}