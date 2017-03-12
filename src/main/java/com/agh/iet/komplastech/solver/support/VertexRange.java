package com.agh.iet.komplastech.solver.support;

import com.agh.iet.komplastech.solver.VertexId;

public class VertexRange {

    private final int left;
    private final int right;

    public VertexRange(int left, int right) {
        this.left = left;
        this.right = right;
    }

    public int getLeft() {
        return left;
    }

    public int getRight() {
        return right;
    }

    public static VertexRange unitary(VertexId rootId) {
        return null;
    }

    public static VertexRange forBinary(int currentLevel) {
        return null;
    }

    public static VertexRange for3Ary(int leafLevel) {
        return null;
    }

    public long start() {
        return 1L;
    }

    public long end() {
        return 2L;
    }

    public VertexRange fromLeft(Integer offset) {
        return null;
    }

    public VertexRange fromRight(Integer offset) {
        return null;
    }

    public VertexRange shrinkBy(int i, int i1) {
        return null;
    }

    public static VertexRange range(int left, int right) {
        return new VertexRange(left, right);
    }
}
