package com.agh.iet.komplastech.solver.results.visualization;

import com.agh.iet.komplastech.solver.SolutionsInTime;
import org.jzy3d.chart.Chart;
import org.jzy3d.maths.Range;

import javax.swing.*;

import java.awt.*;

import static com.agh.iet.komplastech.solver.results.visualization.SurfaceBuilder.aSurface;
import static com.agh.iet.komplastech.solver.results.visualization.SolutionMapper.fromSolution;

public class TimeLapseViewer extends JFrame {

    private final SolutionsInTime solutionsInTime;
    private final SolutionMapper solutionMapper;
    private ChartManager chartManager;

    private JSlider frameSlider;
    private ChartFrame chartView;

    public TimeLapseViewer(SolutionsInTime solutionsInTime) {
        this.solutionsInTime = solutionsInTime;
        solutionMapper = fromSolution(solutionsInTime);
        chartManager = new ChartManager(solutionMapper, solutionsInTime);
        initialize();
        initializeSlider();
    }

    private void initializeSlider() {
        frameSlider = new JSlider(JSlider.HORIZONTAL, 0, solutionsInTime.getTimeStepCount(), 0);
        frameSlider.setMajorTickSpacing(10);
        frameSlider.setMinorTickSpacing(1);
        frameSlider.setPaintTicks(true);
        frameSlider.setPaintLabels(true);
        frameSlider.setVisible(true);
        frameSlider.addChangeListener((e) -> {
            JSlider source = (JSlider) e.getSource();
            if(!source.getValueIsAdjusting()) {
                solutionMapper.setStep(source.getValue());
                redrawFrame();
            }
        });
        getContentPane().add(frameSlider, BorderLayout.SOUTH);
    }

    private void redrawFrame() {
        chartView.redraw();
    }

    private void initialize() {
        setTitle("Simple example");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 800);
        setLocationRelativeTo(null);
        setTitle("test");
        setLayout(new BorderLayout());
        buildChart();
        redrawFrame();
    }

    private void buildChart() {
        chartView = new ChartFrame(chartManager);
        getContentPane().add(chartView, BorderLayout.CENTER);
        addButton("test");
        pack();
    }

    private void addButton(String text) {
        JButton button = new JButton(text);
//        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        getContentPane().add(button, BorderLayout.NORTH);
    }

}
