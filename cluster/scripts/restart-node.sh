#!/usr/bin/env bash

DEPS_DIR=$(pwd)/dependencies
RUN_DIR="hazelcast-instances/hazelcast-3.8-$1"
PUBLIC_ADDRESS=$2
JOIN_INTERFACE=$3
POOL_SIZE=$4

HAZELCAST_CFG=$(pwd)/node-config/hazelcast-agh.xml

function stop {
   ./${RUN_DIR}/bin/stop.sh
   sleep 5
}

function start {
        env MIN_HEAP_SIZE=6G MAX_HEAP_SIZE=6G CLASSPATH=${DEPS_DIR}/* JAVA_OPTS="-Dhazelcast.config=${HAZELCAST_CFG} -Dexecutor.pool.size=${POOL_SIZE} -Dpublic.address=${PUBLIC_ADDRESS} -Djoin.interface=${JOIN_INTERFACE}" ./${RUN_DIR}/bin/start.sh
}


stop
start
