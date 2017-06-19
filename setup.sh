#!/usr/bin/env bash

apt-get update
apt-get -y install \
    openjdk-8-jre \
    git-all

SOLVER_ROOT=$(pwd)
SCRIPTS_DIR=${SOLVER_ROOT}/cluster/scripts

cd ~

export KPTS_SOLVER=${SOLVER_ROOT}
export KPTS_SOLVER_DEPS=${SOLVER_ROOT}/dist
export KPTS_SOLVER_CLIENT=$(pwd)/KptsSolverClient

bash ${SCRIPTS_DIR}/update.sh

rm -f solverCommands node-config dependencies || true
ln -s ${SCRIPTS_DIR} solverCommands
ln -s ${SOLVER_ROOT}/dist dependencies
ln -s ${SOLVER_ROOT}/cluster/config node-config