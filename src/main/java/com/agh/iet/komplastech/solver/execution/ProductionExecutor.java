package com.agh.iet.komplastech.solver.execution;

import com.agh.iet.komplastech.solver.productions.Production;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ProductionExecutor {

    private Logger logger = Logger.getLogger(ProductionExecutor.class);

    private List<Production> productionsToExecute = new ArrayList<>();

    private ExecutorService executor;

    ProductionExecutor(ExecutorService executor, Collection<Production> productionsToExecute) {
        this.executor = executor;
        this.productionsToExecute.addAll(productionsToExecute);
    }

    public void launchProductions() {
        try {
            executor.invokeAll(productionsToExecute.stream().map(
                    (Function<Production, Callable<Void>>) ProductionCallable::new
            ).collect(Collectors.toList()));
        } catch (InterruptedException e) {
            logger.error("Could not run all productions in a set", e);
        }
    }

    private static class ProductionCallable implements Callable<Void>, HazelcastInstanceAware, Serializable {

        private Production production;
        private HazelcastInstance hazelcastInstance;

        private ProductionCallable(Production production) {
            this.production = production;
        }

        @Override
        public Void call() throws Exception {
            production.run();
            return null;
        }

        @Override
        public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
            this.hazelcastInstance = hazelcastInstance;
        }

    }

}
