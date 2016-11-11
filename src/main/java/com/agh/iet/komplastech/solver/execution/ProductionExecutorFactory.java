package com.agh.iet.komplastech.solver.execution;

import com.agh.iet.komplastech.solver.productions.Production;

import java.util.Collection;
import java.util.concurrent.ExecutorService;

import static java.util.Arrays.asList;
import static java.util.concurrent.Executors.newCachedThreadPool;

public class ProductionExecutorFactory {

    private ExecutorService executorService = newCachedThreadPool();

    public ProductionExecutor createLauncherFor(Production... productions) {
        return createLauncherFor(asList(productions));
    }

    public ProductionExecutor createLauncherFor(Collection<Production> productions) {
        return new ProductionExecutor(executorService, productions);
    }

}
