package com.agh.iet.komplastech.solver.logger.process;


import org.apache.log4j.Logger;

public class ConsoleProcessLogger implements ProcessLogger {

    private Logger logger = Logger.getLogger(ConsoleProcessLogger.class);

    @Override
    public void logStageReached(String stageDescription) {
        logger.info(stageDescription);
    }

}
