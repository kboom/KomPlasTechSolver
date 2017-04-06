#!/usr/bin/env bash
rm -rf ./SolverClient
(cd $KPTS_SOLVER && git pull)
mkdir -p .tmp
tar -xvf $KPTS_SOLVER/dist/KomPlasTechSolver.tar -C .tmp/ && mv ./tmp/KomPlasTechSolver KptsSolverClient
rmdir .tmp
