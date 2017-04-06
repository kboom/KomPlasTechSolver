#!/usr/bin/env bash
rm -rf ./SolverClient
(cd $KPTS_SOLVER && git pull)
mkdir .tmp
tar -xvf $KPTS_SOLVER/dist/KomPlasTechSolver.tar -C ~/ && mv ./tmp/KomPlasTechSolver KptsSolverClient
rmdir .tmp
