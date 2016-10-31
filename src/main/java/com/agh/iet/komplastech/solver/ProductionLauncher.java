package com.agh.iet.komplastech.solver;

import com.agh.iet.komplastech.solver.productions.Production;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;
import java.util.stream.Collectors;

class ProductionLauncher {

    private Logger logger = Logger.getLogger(ProductionLauncher.class);

    private List<Production> productionsToExecute = new ArrayList<>();

    private ExecutorService threadPoolExecutor;

    public ProductionLauncher(ExecutorService threadPoolExecutor, Collection<Production> productionsToExecute) {
        this.threadPoolExecutor = threadPoolExecutor;
        this.productionsToExecute.addAll(productionsToExecute);
    }

    public void launchProductions() {
        try {
            threadPoolExecutor.invokeAll(productionsToExecute.stream().map(
                    (Function<Production, Callable<Void>>) ProductionCallable::new
            ).collect(Collectors.toList()));
        } catch (InterruptedException e) {
            logger.error("Could not run all productions in a set", e);
        }
    }

    private static class ProductionCallable implements Callable<Void> {

        private Production production;

        private ProductionCallable(Production production) {
            this.production = production;
        }

        @Override
        public Void call() throws Exception {
            production.run();
            return null;
        }

    }

}
