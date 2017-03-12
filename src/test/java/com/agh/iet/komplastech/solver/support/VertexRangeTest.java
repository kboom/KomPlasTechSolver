package com.agh.iet.komplastech.solver.support;

import org.junit.Test;

import static com.agh.iet.komplastech.solver.support.VertexRange.forBinary;
import static org.assertj.core.api.Assertions.assertThat;

public class VertexRangeTest {

    @Test
    public void canReturnBinaryRange() {
        assertThat(forBinary(1)).isEqualTo(VertexRange.range(1, 1));
    }

}