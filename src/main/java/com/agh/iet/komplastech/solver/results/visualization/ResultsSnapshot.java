package com.agh.iet.komplastech.solver.results.visualization;

import com.agh.iet.komplastech.solver.Solution;
import com.agh.iet.komplastech.solver.support.Mesh;
import org.jzy3d.plot3d.builder.Mapper;

import javax.swing.*;
import java.awt.*;

public class ResultsSnapshot extends JFrame {

    private SurfaceFactory surfaceFactory;
    private ChartFrame chartView;

    public ResultsSnapshot(Mapper mapper, Mesh mesh) {
        attachAskToClose();
        surfaceFactory = new SurfaceFactory(mapper, mesh);
        initialize();
    }

    private void initialize() {
        setTitle("Snapshot of results");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1600, 900);
        setLocationRelativeTo(null);
        setTitle("test");
        setLayout(new BorderLayout());
        buildChart();
        redraw();
    }

    private void redraw() {
        chartView.redraw();
    }

    private void buildChart() {
        chartView = new ChartFrame(surfaceFactory);
        getContentPane().add(chartView, BorderLayout.CENTER);
        pack();
    }

    private void attachAskToClose() {
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (JOptionPane.showConfirmDialog(ResultsSnapshot.this,
                        "Are you sure to close this window?", "Really Closing?",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
                    close();
                }
            }
        });
    }

    public void close() {
        System.exit(0);
    }

    public static ResultsSnapshot fromSolution(Solution solution) {
        SolutionMapper solutionMapper = new SolutionMapper(solution);
        return new ResultsSnapshot(solutionMapper, solution.getMesh());
    }

}
