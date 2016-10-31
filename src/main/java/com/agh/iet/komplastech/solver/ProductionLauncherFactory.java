package com.agh.iet.komplastech.solver;

import com.agh.iet.komplastech.solver.productions.Production;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;

import static java.util.concurrent.Executors.newCachedThreadPool;

class ProductionLauncherFactory {

    private ExecutorService executorService = newCachedThreadPool();

    ProductionLauncher createLauncherFor(Production... productions) {
        return new ProductionLauncher(executorService, Arrays.asList(productions));
    }

}
