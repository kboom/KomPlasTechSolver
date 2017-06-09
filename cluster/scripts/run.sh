#!/usr/bin/env bash

CLUSTER_ADDRESS=$1
STEPS=${4:-1}
MIN_MEM=${5:-6G}
MAX_MEM=${6:-6G}
solveCmd=$KPTS_SOLVER_CLIENT/bin/KomPlasTechSolver

JMX_ENABLE_CFG="-Dhazelcast.jmx=true -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=2099 -Dcom.sun.management.jmxremote.local.only=false -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false"

env JAVA_OPTS="-Xms${MIN_MEM} -Xmx${MAX_MEM} ${JMX_ENABLE_CFG} -Dhazelcast.jmx=true -Dcom.sun.management.jmxremote.port=1099 -Dcom.sun.management.jmxremote.authenticate=false -Dcluster.address=${CLUSTER_ADDRESS}" ${solveCmd} --problem-size $2 --steps ${STEPS} $3
