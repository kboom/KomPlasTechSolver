#!/usr/bin/env bash
rm -rf ./SolverClient
(cd $KPTS_SOLVER && git pull)
tar -xvf $KPTS_SOLVER/dist/KomPlasTechSolver.tar -C ~/SolverClient
