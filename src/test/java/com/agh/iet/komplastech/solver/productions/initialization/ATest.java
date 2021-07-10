package com.agh.iet.komplastech.solver.productions.initialization;

import com.agh.iet.komplastech.solver.productions.LocalProcessingContext;
import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.support.Vertex;
import org.junit.Test;

import static com.agh.iet.komplastech.solver.VertexId.vertexId;
import static com.agh.iet.komplastech.solver.support.Mesh.aMesh;
import static com.agh.iet.komplastech.solver.support.RegionId.regionId;
import static com.agh.iet.komplastech.solver.support.Vertex.aVertex;
import static java.util.Arrays.deepToString;
import static org.assertj.core.api.Assertions.assertThat;

public class ATest {

    private static final int GRID_SIZE = 12;

    private Mesh DUMMY_MESH = aMesh()
            .withElementsX(GRID_SIZE)
            .withElementsY(GRID_SIZE)
            .withResolutionX(12d)
            .withResolutionY(12d)
            .withOrder(2).build();


    @Test
    public void fillsWithQuadraturePoints() {
        Vertex leafNode = aVertex(vertexId(1), regionId(1)).inMesh(DUMMY_MESH).build();
        A a = new A();
        a.apply(new LocalProcessingContext(leafNode));
        assertThat(deepToString(leafNode.m_b.to2DArray())).isEqualTo(
                "[[0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0], " +
                        "[0.0, 0.013888888888888885, 0.12500000000000006, 0.29166666666666685, 0.45833333333333354, 0.6250000000000002, 0.7916666666666662, 0.9583333333333334, 1.1250000000000004, 1.291666666666667, 1.4583333333333317, 1.6250000000000007, 1.7916666666666687, 1.611111111111111, 0.3333333333333335], " +
                        "[0.0, 0.08333333333333336, 0.6388888888888888, 1.3333333333333321, 2.0000000000000013, 2.6666666666666665, 3.3333333333333317, 4.000000000000001, 4.666666666666665, 5.333333333333333, 6.000000000000001, 6.6666666666666625, 7.3333333333333375, 6.5833333333333375, 1.3611111111111112], " +
                        "[0.0, 0.02777777777777778, 0.1944444444444444, 0.37499999999999994, 0.5416666666666667, 0.7083333333333331, 0.875, 1.0416666666666667, 1.2083333333333328, 1.3750000000000004, 1.5416666666666676, 1.7083333333333326, 1.8750000000000002, 1.6805555555555556, 0.34722222222222227], " +
                        "[0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0], " +
                        "[0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0], " +
                        "[0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0]]");
    }

}