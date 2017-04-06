#!/usr/bin/env bash

DEPS_DIR=$KPTS_SOLVER_DEPS
RUN_DIR="hazelcast-3.8-$1"
HAZELCAST_CFG=$KPTS_SOLVER/cluster/config/hazelcast-agh.xml

function stop {
   ./${RUN_DIR}/bin/stop.sh
   sleep 5
}

function start {
        env MIN_HEAP_SIZE=6G MAX_HEAP_SIZE=6G CLASSPATH=${DEPS_DIR}/* JAVA_OPTS="-Dhazelcast.config=${HAZELCAST_CFG}" ./${RUN_DIR}/bin/start.sh
}


stop
start
