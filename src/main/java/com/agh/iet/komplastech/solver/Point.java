package com.agh.iet.komplastech.solver;

class Point {

    private final double x;

    private final double y;

    private final double value;

    static Point solutionPoint(double x, double y, double value) {
        return new Point(x, y, value);
    }

    private Point(double x, double y, double value) {
        this.x = x;
        this.y = y;
        this.value = value;
    }

    double getX() {
        return x;
    }

    double getY() {
        return y;
    }

    double getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point point = (Point) o;

        if (Double.compare(point.x, x) != 0) return false;
        if (Double.compare(point.y, y) != 0) return false;
        return Double.compare(point.value, value) == 0;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(value);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

}
