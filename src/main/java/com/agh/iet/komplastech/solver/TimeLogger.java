package com.agh.iet.komplastech.solver;

class TimeLogger {

    private long totalCreationMs;
    private long totalInitializationMs;
    private long totalFactorizationMs;
    private long totalBackwardSubstitutionMs;
    private long totalSolutionReadingMs;
    private long firstStageMs;
    private long secondStageMs;

    private long timeStop;

    private long startTime;
    private long stopTime;


    TimeLogger() {

    }

    void logCreation() {
        timeStop = getCurrentMillis();
    }

    void logInitialization() {
        long newTime = getCurrentMillis();
        totalCreationMs += newTime - timeStop;
        timeStop = newTime;
    }

    void logFactorization() {
        long newTime = getCurrentMillis();
        totalInitializationMs += newTime - timeStop;
        timeStop = newTime;
    }

    void logBackwardSubstitution() {
        long newTime = getCurrentMillis();
        totalFactorizationMs += newTime - timeStop;
        timeStop = newTime;
    }

    void logSolution() {
        long newTime = getCurrentMillis();
        totalBackwardSubstitutionMs += newTime - timeStop;
        timeStop = newTime;
    }

    public void logSolutionReading() {
        long newTime = getCurrentMillis();
        totalSolutionReadingMs += newTime - timeStop;
        timeStop = newTime;
    }

    public void logFirstStage() {
        firstStageMs = getCurrentMillis();
    }

    public void logSecondStage() {
        secondStageMs = getCurrentMillis();
    }

    public long getTotalCreationMs() {
        return totalCreationMs;
    }

    public long getTotalInitializationMs() {
        return totalInitializationMs;
    }

    public long getTotalFactorizationMs() {
        return totalFactorizationMs;
    }

    public long getTotalBackwardSubstitutionMs() {
        return totalBackwardSubstitutionMs;
    }

    public long getTotalSolutionReadingMs() {
        return totalSolutionReadingMs;
    }

    public long getTotalSolutionMs() {
        return stopTime - startTime;
    }

    public long getFirstStageTimeMs() {
        return secondStageMs - firstStageMs;
    }

    public long getSecondStageTimeMs() {
        return stopTime - secondStageMs;
    }

    private long getCurrentMillis() {
        return System.currentTimeMillis();
    }

    public void logStart() {
        startTime = getCurrentMillis();
    }

    public void logStop() {
        stopTime = getCurrentMillis();
    }
}
