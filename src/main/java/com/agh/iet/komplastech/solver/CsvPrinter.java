package com.agh.iet.komplastech.solver;

import java.util.List;

public class CsvPrinter {

    public static final String NEWLINE = "\r\n";

    public String convertToCsv(SolutionGrid solutionGrid) {
        List<Point> points = solutionGrid.getPoints();
        StringBuilder sb = new StringBuilder();
        sb.append("x,y,val");
        sb.append(NEWLINE);
        for(Point point : points) {
            sb.append(String.format("%.2f,%.2f,%.2f", point.getX(), point.getY(), point.getValue()));
            sb.append(NEWLINE);
        }
        return sb.toString();
    }

}
