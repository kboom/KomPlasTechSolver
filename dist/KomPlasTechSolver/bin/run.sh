#!/bin/sh
threads=(1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20)
problem_sizes=(12 24 48 96 192 384 768 1536)

for p in "${problem_sizes[@]}"
do
        for t in "${threads[@]}"
        do
                timeTook=$(./KomPlasTechSolver $p $t false);
                echo "$p,$t,$timeTook";
        done
done
