package com.agh.iet.komplastech.solver.results.visualization;

import com.agh.iet.komplastech.solver.support.Mesh;
import lombok.AllArgsConstructor;
import org.jzy3d.maths.Range;
import org.jzy3d.plot3d.builder.Mapper;
import org.jzy3d.plot3d.primitives.Shape;

import static com.agh.iet.komplastech.solver.results.visualization.SurfaceBuilder.aSurface;

@AllArgsConstructor
class SurfaceFactory {

    private Mapper solutionMapper;
    private Mesh mesh;

    Shape createSurface() {
        return aSurface()
                .withMapper(solutionMapper)
                .withSquareRange(new Range(0, mesh.getElementsX() - 1))
                .withSteps(mesh.getElementsX()).build();
    }

}
