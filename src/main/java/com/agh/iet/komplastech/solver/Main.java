package com.agh.iet.komplastech.solver;

import com.beust.jcommander.JCommander;

public class Main {

    private static String[] PROGRAM_ARGUMENTS;

    public static void main(String[] args) {
        PROGRAM_ARGUMENTS = args;
        SolverLauncher solverLauncher = new SolverLauncher();
        withInjectedProgramArguments(solverLauncher);
        solverLauncher.launch();
    }

    static <T> T withInjectedProgramArguments(T o) {
        JCommander.newBuilder().acceptUnknownOptions(true).addObject(o).build().parse(PROGRAM_ARGUMENTS);
        return o;
    }

}