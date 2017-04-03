package com.agh.iet.komplastech.solver.support;

import com.agh.iet.komplastech.solver.VertexId;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.agh.iet.komplastech.solver.factories.HazelcastGeneralFactory.GENERAL_FACTORY_ID;
import static com.agh.iet.komplastech.solver.factories.HazelcastGeneralFactory.VERTEX_RANGE;

public class VertexRange implements IdentifiedDataSerializable {

    private int left;
    private int right;

    @SuppressWarnings("unused")
    public VertexRange() {

    }

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
        int position = this.left + offset;
        return new VertexRange(position, position);
    }

    public VertexRange fromRight(Integer offset) {
        int position = this.right - offset;
        return new VertexRange(position, position);
    }

    public VertexRange shrinkBy(int leftPadding, int rightPadding) {
        return new VertexRange(left + leftPadding, right - rightPadding);
    }

    public static VertexRange range(int left, int right) {
        return new VertexRange(left, right);
    }

    public static VertexRange forBinaryAndLastLevel(int leafLevel, int lastLevelMultiplicity) {
        int left = (int) Math.pow(2, leafLevel);
        int right = left + lastLevelMultiplicity * (int) Math.pow(2, leafLevel - 1) - 1;
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

    public List<VertexId> getVerticesInRange() {
        return IntStream.range(left, right + 1)
                .mapToObj(VertexId::vertexId)
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "VertexRange{" +
                "left=" + left +
                ", right=" + right +
                '}';
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeInt(left);
        out.writeInt(right);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        left = in.readInt();
        right = in.readInt();
    }

    @Override
    public int getFactoryId() {
        return GENERAL_FACTORY_ID;
    }

    @Override
    public int getId() {
        return VERTEX_RANGE;
    }

}
