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

    double getResolutionX() {
        return resolutionX;
    }

    double getDx() {
        return resolutionX / elementsX;
    }

    double getDy() {
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

    static MeshBuilder aMesh() {
        return new MeshBuilder();
    }

    static class MeshBuilder {

        private Mesh mesh = new Mesh();

        MeshBuilder withResolutionX(double resolutionX) {
            mesh.resolutionX = resolutionX;
            return this;
        }

        MeshBuilder withResolutionY(double resolutionY) {
            mesh.resolutionY = resolutionY;
            return this;
        }

        MeshBuilder withSizeX(int sizeX) {
            mesh.elementsX = sizeX;
            return this;
        }

        MeshBuilder withSizeY(int sizeY) {
            mesh.elementsY = sizeY;
            return this;
        }

        MeshBuilder withOrder(int order) {
            mesh.splineOrder = order;
            return this;
        }

        Mesh build() {
            mesh.dofsX = mesh.elementsX + mesh.splineOrder;
            mesh.dofsY = mesh.elementsY + mesh.splineOrder;
            return mesh;
        }

    }

}
