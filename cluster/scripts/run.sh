#!/usr/bin/env bash

solveCmd=$KPTS_SOLVER_CLIENT/bin/KomPlasTechSolver

env JAVA_OPTS="-Xms6G -Xmx6G" ${solveCmd} --problem-size $1 --steps 1 $2
