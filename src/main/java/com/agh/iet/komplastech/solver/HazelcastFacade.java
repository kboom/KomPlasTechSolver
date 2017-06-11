package com.agh.iet.komplastech.solver;

import com.agh.iet.komplastech.solver.support.CommonProcessingObject;
import com.hazelcast.core.*;
import com.hazelcast.monitor.NearCacheStats;
import com.hazelcast.util.function.Consumer;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

final class HazelcastFacade {

    private final IExecutorService executorService;

    HazelcastFacade(HazelcastInstance hazelcastInstance) {
        this.executorService = hazelcastInstance.getExecutorService("default");
    }

    void forceGC() {
        launchAtAllNodes((Consumer<HazelcastInstance> & Serializable) (hazelcastInstance)
                -> {
            System.gc();
            System.runFinalization();
            System.out.println("Forcing GC");
        });
    }

    /**
     * Do not attempt to get object which does not exist - it will become cached as NULL value.
     */
    void forceLoadCommons() {
        launchAtAllNodes((Consumer<HazelcastInstance> & Serializable) (hazelcastInstance)
                -> {
            IMap<CommonProcessingObject, Object> commonsMap = hazelcastInstance.getMap("commons");

            NearCacheStats stats = commonsMap.getLocalMapStats().getNearCacheStats();
            System.out.printf("%s (%d entries, %d hits, %d misses, %d evictions, %d expirations)%n",
                    "Commons near cache stats before force-preloading", stats.getOwnedEntryCount(), stats.getHits(), stats.getMisses(),
                    stats.getEvictions(), stats.getExpirations());

//            System.out.println("Before near-cache pre-loaded retrieval memory address: " + commonsMap.get(CommonProcessingObject.SOLUTION));

            // this will break if an entry cannot be found because it calls loadAll underneath which erases all entries
//            Map<CommonProcessingObject, Object> objects = commonsMap.getAll(Arrays.stream(CommonProcessingObject.values()).collect(Collectors.toSet()));
//
            Set<Map.Entry<CommonProcessingObject, Object>> entries = commonsMap.entrySet();
            commonsMap.get(CommonProcessingObject.MESH);
            commonsMap.get(CommonProcessingObject.PROBLEM);

            NearCacheStats stats3 = commonsMap.getLocalMapStats().getNearCacheStats();
            System.out.printf("%s (%d entries, %d hits, %d misses, %d evictions, %d expirations)%n",
                    "Commons near cache stats after entrySet called", stats3.getOwnedEntryCount(), stats3.getHits(), stats3.getMisses(),
                    stats3.getEvictions(), stats3.getExpirations());

            if(entries.stream().map(Map.Entry::getKey).collect(Collectors.toSet()).contains(CommonProcessingObject.SOLUTION)) {
                System.out.println("Before near-cache pre-loaded retrieval memory address: " + commonsMap.get(CommonProcessingObject.SOLUTION));
            }

//
            NearCacheStats stats2 = commonsMap.getLocalMapStats().getNearCacheStats();
            System.out.printf("%s (%d entries, %d hits, %d misses, %d evictions, %d expirations)%n",
                    "Commons near cache stats after get", stats2.getOwnedEntryCount(), stats2.getHits(), stats2.getMisses(),
                    stats2.getEvictions(), stats2.getExpirations());

//            System.out.println("First retrieval memory address: " + commonsMap.get(CommonProcessingObject.SOLUTION));
//            System.out.println("Second retrieval memory address: " + commonsMap.get(CommonProcessingObject.SOLUTION));
//
            System.out.println("K in map: " + entries.stream().map(Map.Entry::getKey).map(CommonProcessingObject::name).collect(Collectors.joining(", ")));
            System.out.println("V in map: " + entries.stream().map(Map.Entry::getValue).map(Object::toString).collect(Collectors.joining(", ")));
        });
    }

    private void launchAtAllNodes(Consumer<HazelcastInstance> hazelcastInstanceConsumer) {
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
