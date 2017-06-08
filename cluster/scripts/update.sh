#!/usr/bin/env bash
rm -rf ./KptsSolverClient
(cd $KPTS_SOLVER && git pull) || true
mkdir -p .tmp
tar -xvf $KPTS_SOLVER/dist/KomPlasTechSolver.tar -C .tmp/ && mv .tmp/KomPlasTechSolver KptsSolverClient
rm -rf .tmp
