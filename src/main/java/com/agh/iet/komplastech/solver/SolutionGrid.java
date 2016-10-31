package com.agh.iet.komplastech.solver;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

public class SolutionGrid {

    private Set<Point> points = new HashSet<>();

    public SolutionGrid() {

    }


    private SolutionGrid(Collection<Point> points) {
        this.points.addAll(points);
    }

    public List<Point> getPoints() {
        List<Point> listOfPoints = points.stream().collect(Collectors.toList());
        Collections.sort(listOfPoints, (o1, o2) -> {
            if(o1.getY() < o2.getY()) {
                return -1;
            } else if(o1.getY() > o2.getY()) {
                return 1;
            } else if(o1.getX() < o2.getX()) {
                return -1;
            } else if(o1.getX() > o1.getX()) {
                return 1;
            } else return 0;
        });
        return listOfPoints;
    }

    public void addPoint(Point point) {
        points.add(point);
    }

    public static SolutionGrid solutionGrid(Point... points) {
        return new SolutionGrid(stream(points).collect(Collectors.toList()));
    }

}
