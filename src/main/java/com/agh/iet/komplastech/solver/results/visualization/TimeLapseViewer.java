package com.agh.iet.komplastech.solver.results.visualization;

import com.agh.iet.komplastech.solver.SolutionsInTime;
import org.jzy3d.maths.Range;

import javax.swing.*;

import java.awt.*;

import static com.agh.iet.komplastech.solver.results.visualization.ChartBuilder.aChart;
import static com.agh.iet.komplastech.solver.results.visualization.SolutionMapper.fromSolution;

public class TimeLapseViewer extends JFrame {

    private final SolutionsInTime solutionsInTime;
    private final SolutionMapper solutionMapper;
    private JSlider frameSlider;

    private ChartFrame plot;

    public TimeLapseViewer(SolutionsInTime solutionsInTime) {
        this.solutionsInTime = solutionsInTime;
        solutionMapper = fromSolution(solutionsInTime);
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
        plot.redraw();
    }

    private void initialize() {
        setTitle("Simple example");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 800);
        setLocationRelativeTo(null);
        setTitle("test");
        setLayout(new BorderLayout());
        showFrame();
    }

    private void showFrame() {
        plot = new ChartFrame(aChart()
                .withMapper(solutionMapper)
                .withSquareRange(new Range(0, solutionsInTime.getProblemSize() - 1))
                .withSteps(solutionsInTime.getProblemSize()).build());
        getContentPane().add(plot, BorderLayout.CENTER);
        addButton("test");
        pack();
    }

    private void addButton(String text) {
        JButton button = new JButton(text);
//        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        getContentPane().add(button, BorderLayout.NORTH);
    }

}
