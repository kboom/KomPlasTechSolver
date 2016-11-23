package com.agh.iet.komplastech.solver;

class TimeLogger {

    private long totalCreationMs;
    private long totalInitializationMs;
    private long totalFactorizationMs;
    private long totalBackwardSubstitutionMs;

    private long startTime;


    TimeLogger() {

    }

    void logCreation() {
        startTime = getCurrentMillis();
    }

    void logInitialization() {
        long newTime = getCurrentMillis();
        totalCreationMs += newTime - startTime;
        startTime = newTime;
    }

    void logFactorization() {
        long newTime = getCurrentMillis();
        totalInitializationMs += newTime - startTime;
        startTime = newTime;
    }

    void logBackwardSubstitution() {
        long newTime = getCurrentMillis();
        totalFactorizationMs += newTime - startTime;
        startTime = newTime;

    }

    void logSolution() {
        long newTime = getCurrentMillis();
        totalBackwardSubstitutionMs += newTime - startTime;
        startTime = newTime;
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

    public long getTotalSolutionMs() {
        return totalCreationMs + totalInitializationMs + totalFactorizationMs + totalBackwardSubstitutionMs;
    }

    private long getCurrentMillis() {
        return System.currentTimeMillis();
    }

}
