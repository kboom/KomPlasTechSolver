package com.agh.iet.komplastech.solver;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

public class SolutionGrid {

    private Set<Point> points = new HashSet<>();

    public SolutionGrid() {

    }


    private SolutionGrid(Collection<Point> points) {
        this.points.addAll(points);
    }

    public void addPoint(Point point) {
        points.add(point);
    }

    public static SolutionGrid solutionGrid(Point... points) {
        return new SolutionGrid(stream(points).collect(Collectors.toList()));
    }

}
