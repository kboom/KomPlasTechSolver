package com.agh.iet.komplastech.solver.problem.flood.terrain;

import com.agh.iet.komplastech.solver.support.Mesh;

import java.util.Set;
import java.util.stream.Stream;

public class FunctionTerrainBuilder {

    public static FunctionTerrainBuilder get() {
        return new FunctionTerrainBuilder();
    }

    public FunctionTerrainBuilder withMesh(Mesh mesh) {
        return this;
    }

    public FunctionTerrainBuilder withFunction() {
        return this;
    }

    public Stream<TerrainPoint> build() {
        return null;
    }
}
