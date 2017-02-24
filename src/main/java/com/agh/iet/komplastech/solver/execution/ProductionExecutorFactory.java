package com.agh.iet.komplastech.solver.execution;

import com.agh.iet.komplastech.solver.productions.Production;

import java.util.Collection;
import java.util.concurrent.ExecutorService;

import static java.util.Arrays.asList;

public class ProductionExecutorFactory {

    private ExecutorService executorService;

    public ProductionExecutorFactory(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public ProductionExecutor createLauncherFor(Production... productions) {
        return createLauncherFor(asList(productions));
    }

    public ProductionExecutor createLauncherFor(Collection<Production> productions) {
        return new ProductionExecutor(executorService, productions);
    }

    public void joinAll() {
        if (!executorService.isShutdown()) {
            executorService.shutdown();
        }
    }

}
