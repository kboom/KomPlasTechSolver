#!/usr/bin/env bash

PROBLEM_SIZES=(12 24 48 96 192 384 768 1536 3072 6144)

function runBatch {
    PROBLEM_SIZE=$1

	echo "Running program with arguments problem size ($PROBLEM_SIZE)"

    ./run.sh ${PROBLEM_SIZE}
}

for ps in "${PROBLEM_SIZES[@]}"
do
    runBatch ${ps}
done