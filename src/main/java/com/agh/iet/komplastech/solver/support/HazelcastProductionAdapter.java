package com.agh.iet.komplastech.solver.support;

import com.agh.iet.komplastech.solver.productions.HazelcastProcessingContext;
import com.agh.iet.komplastech.solver.productions.Production;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.map.AbstractEntryProcessor;

import java.util.Map;

public class HazelcastProductionAdapter extends AbstractEntryProcessor<String, Vertex>
        implements HazelcastInstanceAware {

    private Production production;
    private transient HazelcastInstance hazelcastInstance;

    public HazelcastProductionAdapter(Production production) {
        this.production = production;
    }

    @Override
    public Object process(Map.Entry<String, Vertex> entry) {
        return production.apply(new HazelcastProcessingContext(hazelcastInstance, entry.getValue()));
    }

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }

}