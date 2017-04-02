package com.agh.iet.komplastech.solver.support;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.io.Serializable;

public class Mesh implements DataSerializable {

    private double resolutionX;

    private double resolutionY;

    private int elementsX;

    private int elementsY;

    private int splineOrder;

    private int dofsX;

    private int dofsY;

    private Mesh() {}

    public double getResolutionX() {
        return resolutionX;
    }

    public double getDx() {
        return resolutionX / elementsX;
    }

    public double getDy() {
        return resolutionY / elementsY;
    }

    public double getResolutionY() {
        return resolutionY;
    }

    public int getElementsX() {
        return elementsX;
    }

    public int getElementsY() {
        return elementsY;
    }

    public int getSplineOrder() {
        return splineOrder;
    }

    public int getDofsX() {
        return dofsX;
    }

    public int getDofsY() {
        return dofsY;
    }

    public static MeshBuilder aMesh() {
        return new MeshBuilder();
    }

    public int getCenterX() {
        return elementsX / 2;
    }

    public int getCenterY() {
        return elementsY / 2;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeDouble(resolutionX);
        out.writeDouble(resolutionY);
        out.writeInt(elementsX);
        out.writeInt(elementsY);
        out.writeInt(splineOrder);
        out.writeInt(dofsX);
        out.writeInt(dofsY);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        resolutionX = in.readDouble();
        resolutionY = in.readDouble();
        elementsX = in.readInt();
        elementsY = in.readInt();
        splineOrder = in.readInt();
        dofsX = in.readInt();
        dofsY = in.readInt();
    }

    public static class MeshBuilder {

        private Mesh mesh = new Mesh();

        public MeshBuilder withResolutionX(double resolutionX) {
            mesh.resolutionX = resolutionX;
            return this;
        }

        public MeshBuilder withResolutionY(double resolutionY) {
            mesh.resolutionY = resolutionY;
            return this;
        }

        public MeshBuilder withElementsX(int elementsX) {
            mesh.elementsX = elementsX;
            return this;
        }

        public MeshBuilder withElementsY(int elementsY) {
            mesh.elementsY = elementsY;
            return this;
        }

        public MeshBuilder withOrder(int order) {
            mesh.splineOrder = order;
            return this;
        }

        public Mesh build() {
            mesh.dofsX = mesh.elementsX + mesh.splineOrder;
            mesh.dofsY = mesh.elementsY + mesh.splineOrder;
            return mesh;
        }

    }

    @Override
    public String toString() {
        return "Mesh{" +
                "resolutionX=" + resolutionX +
                ", resolutionY=" + resolutionY +
                ", elementsX=" + elementsX +
                ", elementsY=" + elementsY +
                ", splineOrder=" + splineOrder +
                ", dofsX=" + dofsX +
                ", dofsY=" + dofsY +
                '}';
    }

}
