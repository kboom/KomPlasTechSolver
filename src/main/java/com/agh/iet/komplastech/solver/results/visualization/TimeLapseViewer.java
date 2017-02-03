package com.agh.iet.komplastech.solver.results.visualization;

import com.agh.iet.komplastech.solver.SolutionsInTime;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static com.agh.iet.komplastech.solver.results.visualization.DisplayState.ANIMATION_DISPLAY;
import static com.agh.iet.komplastech.solver.results.visualization.DisplayState.SNAPSHOT;
import static com.agh.iet.komplastech.solver.results.visualization.SolutionMapper.fromSolution;

public class TimeLapseViewer extends JFrame {

    private final SolutionsInTime solutionsInTime;
    private final SolutionMapper solutionMapper;
    private SurfaceFactory surfaceFactory;

    private JSlider frameSlider;
    private ChartFrame chartView;

    private volatile DisplayState displayState = SNAPSHOT;

    private Thread animationThread;

    public TimeLapseViewer(SolutionsInTime solutionsInTime) {
        this.solutionsInTime = solutionsInTime;
        solutionMapper = fromSolution(solutionsInTime);
        surfaceFactory = new SurfaceFactory(solutionMapper, solutionsInTime);
        initialize();
        initializeSlider();
        animate();
        handleClose();
    }

    private void handleClose() {
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (JOptionPane.showConfirmDialog(TimeLapseViewer.this,
                        "Are you sure to close this window?", "Really Closing?",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
                    close();
                }
            }
        });
    }

    private void initializeSlider() {
        frameSlider = new JSlider(JSlider.HORIZONTAL, 0, solutionsInTime.getTimeStepCount(), 0);
        frameSlider.setMajorTickSpacing(10);
        frameSlider.setMinorTickSpacing(1);
        frameSlider.setPaintTicks(true);
        frameSlider.setPaintLabels(true);
        frameSlider.setVisible(true);
        frameSlider.addChangeListener((e) -> {
            if(displayState == SNAPSHOT) {
                JSlider source = (JSlider) e.getSource();
                if(!source.getValueIsAdjusting()) {
                    solutionMapper.setStep(source.getValue());
                    redrawFrame();
                }
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
        chartView = new ChartFrame(surfaceFactory);
        getContentPane().add(chartView, BorderLayout.CENTER);
        getContentPane().add(addButton("toggle animation", this::toggleAnimation), BorderLayout.NORTH);
        pack();
    }

    private void toggleAnimation(ActionEvent e) {
        JToggleButton button = (JToggleButton) e.getSource();
        boolean selected = button.isSelected();
        if(selected) {
            startAnimation();
        } else {
            stopAnimation();
        }
    }

    private void startAnimation() {
        displayState = DisplayState.ANIMATION_DISPLAY;
        animationThread.start();
    }

    private void stopAnimation() {
        displayState = DisplayState.STOPPING;
        try {
            animationThread.join();
            displayState = DisplayState.SNAPSHOT;
        } catch (InterruptedException e1) {
            throw new IllegalStateException(e1);
        }
    }

    private JToggleButton addButton(String text, ActionListener changeListener) {
        JToggleButton button = new JToggleButton(text);
        button.addActionListener(changeListener);
        return button;
    }

    private void animate() {
        animationThread = new Thread(() -> {
            final int timeStepCount = solutionsInTime.getTimeStepCount();
            int timeStep = 0;
            while(displayState == ANIMATION_DISPLAY) {
                if(timeStep >= timeStepCount) {
                    timeStep = 0;
                }
                solutionMapper.setStep(timeStep++);
                frameSlider.setValue(timeStep);
                redrawFrame();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {

                }
            }
        });
    }

    public void close() {
        stopAnimation();
        System.exit(0);
    }

}
