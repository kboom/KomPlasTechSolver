package com.agh.iet.komplastech.solver;

import com.agh.iet.komplastech.solver.support.Point;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

public class SolutionGrid {

    private Set<Point> points = new HashSet<>();

    static SolutionGrid solutionGrid(Point... points) {
        return new SolutionGrid(stream(points).collect(Collectors.toList()));
    }

    private SolutionGrid(Collection<Point> points) {
        this.points.addAll(points);
    }

    public List<Point> getPoints() {
        List<Point> listOfPoints = points.stream().collect(Collectors.toList());
        Collections.sort(listOfPoints, NATURAL_POINT_ORDER_COMPARATOR);
        return listOfPoints;
    }

    void addPoint(Point point) {
        points.add(point);
    }

    private static final Comparator<Point> NATURAL_POINT_ORDER_COMPARATOR = (o1, o2) -> {
        if (o1.getY() < o2.getY()) {
            return -1;
        } else if (o1.getY() > o2.getY()) {
            return 1;
        } else if (o1.getX() < o2.getX()) {
            return -1;
        } else if (o1.getX() > o1.getX()) {
            return 1;
        } else return 0;
    };

}
