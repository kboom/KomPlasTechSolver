#!/usr/bin/env bash

CLUSTER_ADDRESS=$1
STEPS=${4:-1}
MIN_MEM=${5:-6G}
MAX_MEM=${6:-6G}
solveCmd=$KPTS_SOLVER_CLIENT/bin/KomPlasTechSolver

env JAVA_OPTS="-Xms${MIN_MEM} -Xmx${MAX_MEM} -Dcom.sun.management.jmxremote -Dhazelcast.jmx=true -Dcluster.address=${CLUSTER_ADDRESS}" ${solveCmd} --problem-size $2 --steps ${STEPS} $3
