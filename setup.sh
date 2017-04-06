#!/usr/bin/env bash

SOLVER_ROOT=$(pwd)
SCRIPTS_DIR=${SOLVER_ROOT}/cluster/scripts

cd ~
chmod 775 ${SCRIPTS_DIR}/*
bash ./${SCRIPTS_DIR}/update.sh
ln -s solverCommands ${SCRIPTS_DIR}

export KPTS_SOLVER=${SOLVER_ROOT}
export KPTS_SOLVER_DEPS=${SOLVER_ROOT}/dist
export KPTS_SOLVER_CLIENT=$(pwd)/SolverClient