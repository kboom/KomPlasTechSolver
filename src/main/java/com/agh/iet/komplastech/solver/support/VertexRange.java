package com.agh.iet.komplastech.solver.support;

import com.agh.iet.komplastech.solver.VertexId;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.agh.iet.komplastech.solver.factories.HazelcastGeneralFactory.GENERAL_FACTORY_ID;
import static com.agh.iet.komplastech.solver.factories.HazelcastGeneralFactory.VERTEX_RANGE;

public class VertexRange implements Serializable, IdentifiedDataSerializable {

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

    public static VertexRange unitary(int id) {
        return range(id, id);
    }

    public static VertexRange forBinary(int currentLevel) {
        return range((int) Math.pow(2, currentLevel), (int) Math.pow(2, currentLevel + 1) - 1);
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

    public int getHeight() {
        return log2(left);
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

    public static VertexRange forNode(int absoluteIndex, int lastLevelCount) {
        int totalHeight = log2(lastLevelCount / 3) + 1;
        VertexRange leafRange = forBinaryAndLastLevel(totalHeight, 3);
        if(leafRange.containsIndex(absoluteIndex)) {
            return leafRange;
        } else {
            int currentHeight = log2(absoluteIndex);
            return forBinary(currentHeight);
        }
    }

    private boolean containsIndex(int absoluteIndex) {
        return left <= absoluteIndex && absoluteIndex <= right;
    }

    public boolean includes(VertexId vertexId) {
        return containsIndex(vertexId.getAbsoluteIndex());
    }

    private static int log2(int index) {
        return (int) Math.floor(Math.log10(index) / Math.log10(2));
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
