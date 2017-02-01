package com.agh.iet.komplastech.solver.results.visualization;

import org.jzy3d.chart.Chart;
import org.jzy3d.chart.factories.AWTChartComponentFactory;
import org.jzy3d.chart.factories.IChartComponentFactory;
import org.jzy3d.colors.Color;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.ColorMapRainbow;
import org.jzy3d.maths.Range;
import org.jzy3d.plot3d.builder.Builder;
import org.jzy3d.plot3d.builder.Mapper;
import org.jzy3d.plot3d.builder.concrete.OrthonormalGrid;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.rendering.canvas.Quality;

public class ChartBuilder {

    private Mapper mapper;
    private Range range;
    private int steps;

    private ChartBuilder() {

    }

    public static ChartBuilder aChart() {
        return new ChartBuilder();
    }

    public ChartBuilder withMapper(Mapper mapper) {
        this.mapper = mapper;
        return this;
    }

    public ChartBuilder withSquareRange(Range range) {
        this.range = range;
        return this;
    }

    public ChartBuilder withSteps(int steps) {
        this.steps = steps;
        return this;
    }

    public Chart build() {
        Shape surface = Builder.buildOrthonormal(new OrthonormalGrid(range, steps, range, steps), mapper);
        surface.setColorMapper(new ColorMapper(new ColorMapRainbow(), surface.getBounds().getZmin(), surface.getBounds().getZmax(), new Color(1, 1, 1, .5f)));
        surface.setWireframeDisplayed(false);
        surface.setWireframeColor(Color.WHITE);
        surface.setFaceDisplayed(true);

        Chart chart = AWTChartComponentFactory.chart(
                Quality.Advanced,
                IChartComponentFactory.Toolkit.swing);

        chart.getScene().getGraph().add(surface);
        return chart;
    }

}
