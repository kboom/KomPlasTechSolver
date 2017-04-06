package com.agh.iet.komplastech.solver.logger.process;

import com.agh.iet.komplastech.solver.productions.Production;
import com.agh.iet.komplastech.solver.support.RegionId;
import com.agh.iet.komplastech.solver.support.VertexReference;

import java.util.Set;

public interface ProcessLogger {

    void logStageReached(String stageDescription);

    void logProductionLaunched(Production production, Set<VertexReference> vertexBatch);

}
