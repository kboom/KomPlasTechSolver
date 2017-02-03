package com.agh.iet.komplastech.solver.results.visualization;

import org.jzy3d.chart.Chart;
import org.jzy3d.chart.controllers.mouse.camera.AWTCameraMouseController;

import javax.swing.*;
import java.awt.*;

class ChartFrame extends JPanel {

    private static final Dimension PREFERRED_SIZE = new Dimension(600, 600);

    private final Chart chart;

    ChartFrame(Chart chart) {
        this.chart = chart;
        initialize();
    }

    private void initialize() {
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

}
