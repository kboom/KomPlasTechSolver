package com.agh.iet.komplastech.solver;

import com.beust.jcommander.JCommander;

public class Main {

    public static void main(String[] args) {
        SolverLauncher solverLauncher = new SolverLauncher();
        new JCommander(solverLauncher, args);
        solverLauncher.launch();
    }

}