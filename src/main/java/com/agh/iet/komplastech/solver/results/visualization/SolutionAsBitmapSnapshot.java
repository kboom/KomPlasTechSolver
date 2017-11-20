package com.agh.iet.komplastech.solver.results.visualization;

import com.agh.iet.komplastech.solver.Solution;
import com.agh.iet.komplastech.solver.support.Mesh;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.*;

import static com.agh.iet.komplastech.solver.support.MatrixUtils.maxValueOf;

public class SolutionAsBitmapSnapshot extends JFrame {

    public SolutionAsBitmapSnapshot(Solution solution) throws HeadlessException {
        initialize(solution);
    }

    private void initialize(Solution solution) {
        setTitle("Snapshot of results");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 820);
        setPreferredSize(new Dimension(800, 820));
        setLocationRelativeTo(null);
        setTitle("test");
        setLayout(new BorderLayout());

        final Mesh mesh = solution.getMesh();
        final double[][] rhs = solution.getRhs();

        byte[] buffer = new byte[mesh.getElementsX() * mesh.getElementsY()];

        double maxValue = maxValueOf(rhs);
        double minValue = maxValueOf(rhs);
        double offset = minValue < 0 ? Math.abs(minValue) : 0;

        for (int i = 0; i < mesh.getElementsY(); i++) {
            for (int j = 0; j < mesh.getElementsX(); j++) {
                double value = rhs[i + 1][j + 1];
                buffer[(i * mesh.getElementsY()) + j] = (byte) (255 - (((value + offset) / (maxValue + offset)) * 255));
            }
        }

        BufferedImage image = getGrayscale(mesh.getElementsY(), buffer); // image.getScaledInstance(1600, 900, Image.SCALE_FAST);

        add(new ImagePanel(image), BorderLayout.CENTER);
    }

    public class ImagePanel extends JComponent {

        private final BufferedImage image;

        ImagePanel(BufferedImage image) {
            this.image = image;
        }

        @Override
        protected void paintComponent(Graphics g){
            super.paintComponent(g);
            Graphics2D tGraphics2D = (Graphics2D) g;
            tGraphics2D.setBackground(Color.WHITE);
            tGraphics2D.setPaint(Color.WHITE);
            tGraphics2D.fillRect( 0, 0, 800, 800);
            tGraphics2D.setRenderingHint( RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
            tGraphics2D.drawImage( image, 0, 0, 800, 800, null );
        }

    }

//    @Override
//    public void paint(Graphics g) {
//        g.drawImage(image, 0, 0, null);
//    }

    public static BufferedImage getGrayscale(int width, byte[] buffer) {
        int height = buffer.length / width;
        ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
        int[] nBits = { 8 };
        ColorModel cm = new ComponentColorModel(cs, nBits, false, true,
                Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
        SampleModel sm = cm.createCompatibleSampleModel(width, height);
        DataBufferByte db = new DataBufferByte(buffer, width * height);
        WritableRaster raster = Raster.createWritableRaster(sm, db, null);
        return new BufferedImage(cm, raster, false, null);
    }

}
