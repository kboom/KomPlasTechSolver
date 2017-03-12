package com.agh.iet.komplastech.solver.support;

import org.junit.Test;

import static com.agh.iet.komplastech.solver.support.VertexRange.forBinary;
import static org.assertj.core.api.Assertions.assertThat;

public class VertexRangeTest {

    @Test
    public void binaryRangeOf0Is1To1() {
        assertThat(forBinary(0)).isEqualTo(VertexRange.range(1, 1));
    }

    @Test
    public void binaryRangeOf1Is2To3() {
        assertThat(forBinary(1)).isEqualTo(VertexRange.range(2, 3));
    }

    @Test
    public void binaryRangeOf3Is4To7() {
        assertThat(forBinary(2)).isEqualTo(VertexRange.range(4, 7));
    }

}