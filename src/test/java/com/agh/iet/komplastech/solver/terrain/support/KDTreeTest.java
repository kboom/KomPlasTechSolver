package com.agh.iet.komplastech.solver.terrain.support;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class KDTreeTest {

    @Test
    public void canFindNearest() {
        KDTree kdTree = new KDTree(4);
        kdTree.add(new double[] { 1, 2 });
        kdTree.add(new double[] { 3, 4 });
        kdTree.add(new double[] { 6, -6 });
        kdTree.add(new double[] { 100, 4 });
        assertThat(kdTree.findNearest(new double[] { 4, 5 })).isEqualTo(new double[] { 3, 4 });
    }

}