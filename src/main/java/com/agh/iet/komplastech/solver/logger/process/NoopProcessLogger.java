package com.agh.iet.komplastech.solver.logger.process;

import com.agh.iet.komplastech.solver.productions.Production;
import com.agh.iet.komplastech.solver.support.VertexReference;

import java.util.Set;

public class NoopProcessLogger implements ProcessLogger {

    @Override
    public void logStageReached(String stageDescription) {

    }

    @Override
    public void logProductionLaunched(Production production, Set<VertexReference> vertexBatch) {

    }

}
