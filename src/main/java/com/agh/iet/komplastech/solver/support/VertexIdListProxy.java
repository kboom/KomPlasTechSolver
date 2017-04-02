package com.agh.iet.komplastech.solver.support;

import com.agh.iet.komplastech.solver.VertexId;

public interface VertexIdListProxy {

    /**
     * This definitely must be done in batches or once at the end.
     * @param currentLevel
     * @param key
     */
    void add(int currentLevel, VertexId key);

}
