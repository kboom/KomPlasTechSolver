package com.agh.iet.komplastech.solver.results.visualization;

import org.jzy3d.chart.Chart;
import org.jzy3d.chart.controllers.mouse.camera.AWTCameraMouseController;

import javax.swing.*;
import java.awt.*;

public class ChartFrame extends JFrame {

    private final Chart chart;

    public ChartFrame(Chart chart) {
        this.chart = chart;
        initialize();
    }

    private void initialize() {
        setTitle("Simple example");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        add((Component) chart.getCanvas());
        setSize(800, 800);
        setLocationRelativeTo(null);
        setTitle("test");
        AWTCameraMouseController controller = new AWTCameraMouseController(chart);
        addMouseListener(controller);
        addMouseMotionListener(controller);
        addMouseWheelListener(controller);
    }

}
