package com.agh.iet.komplastech.solver;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.IExecutorService;
import com.hazelcast.core.Member;
import com.hazelcast.util.function.Consumer;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

final class HazelcastFacade {

    private final IExecutorService executorService;

    HazelcastFacade(HazelcastInstance hazelcastInstance) {
        this.executorService = hazelcastInstance.getExecutorService("default");
    }

    void forceLoadCommons() {
        forceLoad((Consumer<HazelcastInstance> & Serializable) (hazelcastInstance)
                -> hazelcastInstance.getMap("commons").entrySet());
    }

    void forceLoad(Consumer<HazelcastInstance> hazelcastInstanceConsumer) {
        Map<Member, Future> map = executorService.submitToAllMembers(new TaskLauncher(hazelcastInstanceConsumer));
        map.forEach((member, future) -> {
            try {
                future.get();
            } catch (Exception e) {
                throw new IllegalStateException("Could not preload commons on at least one member", e);
            }
        });
    }

    private final static class TaskLauncher implements Callable, HazelcastInstanceAware, Serializable {

        private HazelcastInstance hazelcastInstance;
        private Consumer<HazelcastInstance> hazelcastInstanceConsumer;

        TaskLauncher(Consumer<HazelcastInstance> hazelcastInstanceConsumer) {
            this.hazelcastInstanceConsumer = hazelcastInstanceConsumer;
        }

        @Override
        public final void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
            this.hazelcastInstance = hazelcastInstance;
        }

        @Override
        public Void call() {
            hazelcastInstanceConsumer.accept(hazelcastInstance);
            return null;
        }

    }

}
