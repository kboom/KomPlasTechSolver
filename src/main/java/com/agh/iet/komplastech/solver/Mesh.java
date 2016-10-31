package com.agh.iet.komplastech.solver;

public class Mesh {

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

        public MeshBuilder withSizeX(int sizeX) {
            mesh.elementsX = sizeX;
            return this;
        }

        public MeshBuilder withSizeY(int sizeY) {
            mesh.elementsY = sizeY;
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

}
