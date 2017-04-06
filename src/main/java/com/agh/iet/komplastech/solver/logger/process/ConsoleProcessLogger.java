package com.agh.iet.komplastech.solver.logger.process;


import com.agh.iet.komplastech.solver.productions.Production;
import com.agh.iet.komplastech.solver.support.RegionId;
import com.agh.iet.komplastech.solver.support.VertexReference;
import org.apache.log4j.Logger;

import java.util.Set;

import static java.lang.String.format;

public class ConsoleProcessLogger implements ProcessLogger {

    private Logger logger = Logger.getLogger(ConsoleProcessLogger.class);

    @Override
    public void logStageReached(String stageDescription) {
        logger.info(stageDescription);
    }

    @Override
    public void logProductionLaunched(Production production, Set<VertexReference> vertexBatch) {
        RegionId regionId = vertexBatch.iterator().next().getRegionId();
        logger.debug(format("Production %s launched for region %d with %d vertices",
                production, regionId.toInt(), vertexBatch.size()));
    }

}
