#!/usr/bin/env bash

DEPS_DIR=$(pwd)/dependencies
RUN_DIR="hazelcast-instances/hazelcast-3.8-$1"
PUBLIC_ADDRESS=$2
JOIN_INTERFACE=$3
POOL_SIZE=${4:-10}
MIN_HEAP=${5:-500M}
MAX_HEAP=${6:-6G}
PARTITION_COUNT=${PARTITION_COUNT:-271}

HAZELCAST_CFG=$(pwd)/node-config/hazelcast-agh.xml

JMX_ENABLE_CFG=""#"-Dhazelcast.jmx=true -Dcom.sun.management.jmxremote.port=8090 -Dcom.sun.management.jmxremote.rmi.port=8090 -Djava.rmi.server.hostname=127.0.0.1 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false"

function stop {
   ./${RUN_DIR}/bin/stop.sh
   sleep 5
}

function start {
        env MIN_HEAP_SIZE=${MIN_HEAP} MAX_HEAP_SIZE=${MAX_HEAP} CLASSPATH=${DEPS_DIR}/* JAVA_OPTS="${JMX_ENABLE_CFG} -Dhazelcast.config=${HAZELCAST_CFG} -Dexecutor.pool.size=${POOL_SIZE} -Dpublic.address=${PUBLIC_ADDRESS} -Djoin.interface=${JOIN_INTERFACE} -Dpartition.count=${PARTITION_COUNT}" ./${RUN_DIR}/bin/start.sh
}


stop
start
