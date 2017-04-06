#!/usr/bin/env bash

solveCmd=$KPTS_SOLVER_CLIENT/bin/KomPlasTechSolver

env JAVA_OPTS="-Xms8G -Xmx12G" ${solveCmd} --problem-size $1 --steps 1 --log-process $2
