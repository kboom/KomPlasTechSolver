package com.agh.iet.komplastech.solver.productions;

import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.support.Vertex;
import com.hazelcast.map.EntryBackupProcessor;
import com.hazelcast.map.EntryProcessor;

import java.util.Map;

/**
 * A production is entry processor as it should be executed on the node that data actually resides in.
 * This makes it run without unnecessary network overhead associated with data read prior to write.
 *
 * A production can only modify its own node.
 */
public abstract class Production implements EntryProcessor<String, Vertex> {

    protected final Mesh mesh;

    public Production(Mesh mesh) {
        this.mesh = mesh;
    }

    @Override
    public Vertex process(Map.Entry<String, Vertex> entry) {
        Vertex vertex = entry.getValue();
        return apply(vertex);
    }

    public abstract Vertex apply(Vertex v);

    @Override
    public EntryBackupProcessor<String, Vertex> getBackupProcessor() {
        return null;
    }

}