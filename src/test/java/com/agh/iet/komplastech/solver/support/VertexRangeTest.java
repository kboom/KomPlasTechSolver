package com.agh.iet.komplastech.solver.support;

import org.junit.Test;

import static com.agh.iet.komplastech.solver.support.VertexRange.*;
import static org.assertj.core.api.Assertions.assertThat;

public class VertexRangeTest {

    @Test
    public void binaryRangeOf0Is1To1() {
        assertThat(forBinary(0)).isEqualTo(range(1, 1));
    }

    @Test
    public void binaryRangeOf1Is2To3() {
        assertThat(forBinary(1)).isEqualTo(range(2, 3));
    }

    @Test
    public void binaryRangeOf3Is4To7() {
        assertThat(forBinary(2)).isEqualTo(range(4, 7));
    }

    @Test
    public void firstItemToTheLeftOfRange16To39Is16() {
        assertThat(range(16, 39).fromLeft(0)).isEqualTo(range(16,16));
    }

    @Test
    public void firstItemToTheLeftOfRange16To39Is17() {
        assertThat(range(16, 39).fromLeft(1)).isEqualTo(range(17,17));
    }

    @Test
    public void firstItemFromTheRightOfRange16To39Is39() {
        assertThat(range(16, 39).fromRight(0)).isEqualTo(range(39,39));
    }

    @Test
    public void firstItemFromTheRightOfRange16To39Is38() {
        assertThat(range(16, 39).fromRight(1)).isEqualTo(range(38,38));
    }

    @Test
    public void range16To39ShrunkBy0Is16To39() {
        assertThat(range(16, 39).shrinkBy(0, 0)).isEqualTo(range(16,39));
    }

    @Test
    public void range16To39ShrunkBy1Is17To38() {
        assertThat(range(16, 39).shrinkBy(1, 1)).isEqualTo(range(17,38));
    }

    @Test
    public void lastRange3ForLevel3Is8To15() {
        assertThat(forBinaryAndLastLevel(3, 3)).isEqualTo(range(8,19));
    }

    @Test
    public void lastRange3ForLevel4Is16To39() {
        assertThat(forBinaryAndLastLevel(4, 3)).isEqualTo(range(16,39));
    }

    @Test
    public void lastRange3ForLevel2Is4To6() {
        assertThat(forBinaryAndLastLevel(2, 3)).isEqualTo(range(4,9));
    }

    @Test
    public void heightOf4To7Is2() {
        assertThat(range(4, 7).getHeight()).isEqualTo(2);
    }

    @Test
    public void heightOf8To15Is3() {
        assertThat(range(8, 15).getHeight()).isEqualTo(3);
    }

    @Test
    public void rangeFor1Is1To1() {
        assertThat(VertexRange.forNode(1)).isEqualTo(range(1, 1));
    }

    @Test
    public void rangeFor2Is2To3() {
        assertThat(VertexRange.forNode(2)).isEqualTo(range(2, 3));
    }

    @Test
    public void rangeFor5Is4To7() {
        assertThat(VertexRange.forNode(5)).isEqualTo(range(4, 7));
    }

}