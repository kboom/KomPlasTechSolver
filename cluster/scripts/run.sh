#!/usr/bin/env bash

CLUSTER_ADDRESS=$1
solveCmd=$KPTS_SOLVER_CLIENT/bin/KomPlasTechSolver

env JAVA_OPTS="-Xms6G -Xmx6G -Dcluster.address=${CLUSTER_ADDRESS}" ${solveCmd} --problem-size $2 --steps 1 $3
