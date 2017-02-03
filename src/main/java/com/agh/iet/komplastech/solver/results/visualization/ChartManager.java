package com.agh.iet.komplastech.solver.results.visualization;

import com.agh.iet.komplastech.solver.SolutionsInTime;
import com.sun.pisces.Surface;
import org.jzy3d.chart.Chart;
import org.jzy3d.maths.Range;
import org.jzy3d.plot3d.builder.Mapper;
import org.jzy3d.plot3d.primitives.Shape;

import static com.agh.iet.komplastech.solver.results.visualization.SurfaceBuilder.aSurface;

public class ChartManager {

    private Mapper solutionMapper;
    private SolutionsInTime solutionsInTime;

    public ChartManager(Mapper solutionMapper, SolutionsInTime solutionsInTime) {
        this.solutionMapper = solutionMapper;
        this.solutionsInTime = solutionsInTime;
    }

    public Shape createSurface() {
        return aSurface()
                .withMapper(solutionMapper)
                .withSquareRange(new Range(0, solutionsInTime.getProblemSize() - 1))
                .withSteps(solutionsInTime.getProblemSize()).build();
    }

}
