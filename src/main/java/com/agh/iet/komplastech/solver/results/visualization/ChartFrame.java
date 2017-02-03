package com.agh.iet.komplastech.solver.results.visualization;

import org.jzy3d.chart.Chart;
import org.jzy3d.chart.controllers.mouse.camera.AWTCameraMouseController;
import org.jzy3d.chart.factories.AWTChartComponentFactory;
import org.jzy3d.chart.factories.IChartComponentFactory;
import org.jzy3d.plot3d.primitives.*;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.rendering.canvas.Quality;

import javax.swing.*;
import java.awt.*;

class ChartFrame extends JPanel {

    private static final Dimension PREFERRED_SIZE = new Dimension(600, 600);

    private final ChartManager chartManager;

    private Chart chart;

    ChartFrame(ChartManager chartManager) {
        this.chartManager = chartManager;
        initialize();
    }

    private void initialize() {
        chart = AWTChartComponentFactory.chart(
                Quality.Advanced,
                IChartComponentFactory.Toolkit.swing);

        Component canvas = (Component) chart.getCanvas();
        canvas.setPreferredSize(PREFERRED_SIZE);
        add(canvas);
        setVisible(true);
        setPreferredSize(PREFERRED_SIZE);
        canvas.setBounds(getBounds());
        AWTCameraMouseController controller = new AWTCameraMouseController(chart);
        addMouseListener(controller);
        addMouseMotionListener(controller);
        addMouseWheelListener(controller);
    }

    public void redraw() {
        Shape newSurface = chartManager.createSurface();
        chart.getScene().getGraph().getAll().clear();
        chart.getScene().getGraph().add(newSurface);
    }
}
