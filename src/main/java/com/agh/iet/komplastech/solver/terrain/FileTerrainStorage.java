package com.agh.iet.komplastech.solver.terrain;

import lombok.Builder;
import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Builder
public class FileTerrainStorage implements TerrainStorage {

    private String inFilePath;

    @Override
    @SneakyThrows
    public Stream<TerrainPoint> loadTerrainPoints() {
        return Files.lines(Paths.get(inFilePath)).map(line -> {
            String[] values = line.split(" ");
            return new TerrainPoint(Double.parseDouble(values[0]), Double.parseDouble(values[1]), Double.parseDouble(values[2]));
        });
    }

    @Override
    public void saveTerrainPoints(Stream<TerrainPoint> terrainPointStream) {

    }

}
