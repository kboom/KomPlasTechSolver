#!/usr/bin/env bash
rm -rf ./SolverClient
git -C $KPTS_SOLVER pull
tar -xvf $KPTS_SOLVER/dist/KomPlasTechSolver.tar -C ~/SolverClient
