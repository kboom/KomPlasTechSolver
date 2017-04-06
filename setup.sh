#!/usr/bin/env bash

SOLVER_ROOT=$(pwd)
SCRIPTS_DIR=${SOLVER_ROOT}/cluster/scripts

cd ~
chmod 775 ${SCRIPTS_DIR}/*
bash ${SCRIPTS_DIR}/update.sh

rm -f solverCommands node-config dependencies
ln -s ${SCRIPTS_DIR} solverCommands
ln -s ${SOLVER_ROOT}/dist dependencies
ln -s ${SOLVER_ROOT}/cluster/config node-config

export KPTS_SOLVER=${SOLVER_ROOT}
export KPTS_SOLVER_DEPS=${SOLVER_ROOT}/dist
export KPTS_SOLVER_CLIENT=$(pwd)/KptsSolverClient