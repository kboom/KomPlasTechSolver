#!/usr/bin/env bash

CLUSTER_ADDRESS=$1

PROBLEM_SIZES=(6144 12288)

MIN_REGION_HEIGHT=${MIN_REGION_HEIGHT-3}
REGION_HEIGHT_STEP=${REGION_HEIGHT_STEP-1}
MAX_REGION_HEIGHT=${MAX_REGION_HEIGHT-7}

MIN_BATCH_SIZE=${MIN_BATCH_SIZE-1}
BATCH_SIZE_STEP=${BATCH_SIZE_STEP-10}
MAX_BATCH_SIZE=${MAX_BATCH_SIZE-51}

function runBatch {
    T_PROBLEM_SIZE=$1
    T_REGION_HEIGHT=$2
    T_BATCH_RATIO=$3
    T_MAX_BATCH_SIZE=$4

	echo "Running program with arguments problem size ($T_PROBLEM_SIZE), region height ($T_REGION_HEIGHT), batch ratio ($T_BATCH_RATIO), max batch size ($T_MAX_BATCH_SIZE)"

    ./solverCommands/run.sh ${CLUSTER_ADDRESS} ${T_PROBLEM_SIZE} "--batch-ratio ${T_BATCH_RATIO} --max-batch-size ${T_MAX_BATCH_SIZE} --region-height ${T_REGION_HEIGHT}"
}

for ps in "${PROBLEM_SIZES[@]}"
do
    for (( rh=${MIN_REGION_HEIGHT}; rh <= ${MAX_REGION_HEIGHT}; rh += ${REGION_HEIGHT_STEP} ));
    do

        for (( bs=${MIN_BATCH_SIZE}; bs <= ${MAX_BATCH_SIZE}; bs += ${BATCH_SIZE_STEP} ));
        do
            runBatch ${ps} ${rh} 1 ${bs}
        done

    done
done
