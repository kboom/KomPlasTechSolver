package com.agh.iet.komplastech.solver.results.visualization;

import org.jzy3d.chart.Chart;
import org.jzy3d.chart.ChartScene;
import org.jzy3d.chart.controllers.mouse.camera.AWTCameraMouseController;
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

import javax.swing.*;
import java.awt.*;

public class PlotIn3D extends JFrame {

    public PlotIn3D() {
        initUI();
    }

    private void initUI() {
        setTitle("Simple example");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void open() {
        Chart chart = getChart();
        add((Component) chart.getCanvas());
        setSize(800, 800);
        setLocationRelativeTo(null);
        setTitle("test");
        setVisible(true);
    }

    Chart getChart() {
        // Define a function to plot
        Mapper mapper = new Mapper() {
            public double f(double x, double y) {
                return x + y;
            }
        };

        // Define range and precision for the function to plot
        Range range = new Range(-150, 150);
        int steps = 50;

        // Create the object to represent the function over the given range.
        Shape surface = Builder.buildOrthonormal(new OrthonormalGrid(range, steps, range, steps), mapper);
        surface.setColorMapper(new ColorMapper(new ColorMapRainbow(), surface.getBounds().getZmin(), surface.getBounds().getZmax(), new Color(1, 1, 1, .5f)));
        surface.setWireframeDisplayed(false);
        surface.setWireframeColor(Color.WHITE);
        surface.setFaceDisplayed(true);

        // Create a chart
        Chart chart = AWTChartComponentFactory.chart(
                Quality.Advanced,
                IChartComponentFactory.Toolkit.swing);

        AWTCameraMouseController controller = new AWTCameraMouseController(chart);

        chart.getScene().getGraph().add(surface);

        addMouseListener(controller);
        addMouseMotionListener(controller);
        addMouseWheelListener(controller);

        return chart;
    }

}
