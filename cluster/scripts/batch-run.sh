#!/usr/bin/env bash

PROBLEM_SIZE=3072

REGION_HEIGHT_STEP=1
MIN_REGION_HEIGHT=1
MAX_REGION_HEIGHT=11

MIN_BATCH_SIZE=1
BATCH_SIZE_STEP=10
MAX_BATCH_SIZE=100

function runBatch {
    PROBLEM_SIZE=$1
    REGION_HEIGHT=$2
    BATCH_RATIO=$3
    MAX_BATCH_SIZE=$4

	echo "Running program with arguments problem size ($PROBLEM_SIZE), region height ($REGION_HEIGHT), batch ratio ($BATCH_RATIO), max batch size ($MAX_BATCH_SIZE)"

    ./solverCommands/run.sh ${PROBLEM_SIZE} "--batch-ratio ${BATCH_RATIO} --max-batch-size ${MAX_BATCH_SIZE} --region-height ${REGION_HEIGHT}"
}

for rh in `seq ${MIN_REGION_HEIGHT} ${MAX_REGION_HEIGHT} ${REGION_HEIGHT_STEP}`;
do
    for bs in `seq ${MIN_BATCH_SIZE} ${MAX_BATCH_SIZE} ${BATCH_SIZE_STEP}`;
    do
        runBatch ${PROBLEM_SIZE} ${rh} 1 ${bs}
    done
done
