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
        return range(rootId.getAbsoluteIndex(), rootId.getAbsoluteIndex());
    }

    public static VertexRange forBinary(int currentLevel) {
        return range((int) Math.pow(2, currentLevel), (int) Math.pow(2, currentLevel + 1) - 1);
    }

    public long start() {
        return left;
    }

    public long end() {
        return right;
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

    public static VertexRange forBinaryAndLastLevel(int leafLevel, double lastLevelCount) {
        int left = (int) Math.pow(2, leafLevel);
        int right = (int) (3.0 / 2.0 * Math.pow(2, leafLevel) - 1);
        return range(left, right);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VertexRange that = (VertexRange) o;

        if (left != that.left) return false;
        return right == that.right;

    }

    @Override
    public int hashCode() {
        int result = left;
        result = 31 * result + right;
        return result;
    }

}
