#!/usr/bin/env bash

<<<<<<< HEAD
#PROBLEM_SIZES=(12 24 48 96 192 384 768 1536)
PROBLEM_SIZES=(1536)
=======
PROBLEM_SIZES=(12 24 48 96 192 384 768 1536)
>>>>>>> 6fe4270e5af4ec0184dc24ab62fc2ad3db10d956

MIN_REGION_HEIGHT=${MIN_REGION_HEIGHT-1}
REGION_HEIGHT_STEP=${REGION_HEIGHT_STEP-1}
MAX_REGION_HEIGHT=${MAX_REGION_HEIGHT-11}

MIN_BATCH_SIZE=${MIN_BATCH_SIZE-1}
<<<<<<< HEAD
BATCH_SIZE_STEP=${BATCH_SIZE_STEP-5}
MAX_BATCH_SIZE=${MAX_BATCH_SIZE-11}
=======
BATCH_SIZE_STEP=${BATCH_SIZE_STEP-10}
MAX_BATCH_SIZE=${MAX_BATCH_SIZE-100}
>>>>>>> 6fe4270e5af4ec0184dc24ab62fc2ad3db10d956

function runBatch {
    T_PROBLEM_SIZE=$1
    T_REGION_HEIGHT=$2
    T_BATCH_RATIO=$3
    T_MAX_BATCH_SIZE=$4

	echo "Running program with arguments problem size ($T_PROBLEM_SIZE), region height ($T_REGION_HEIGHT), batch ratio ($T_BATCH_RATIO), max batch size ($T_MAX_BATCH_SIZE)"

    ./solverCommands/run.sh ${T_PROBLEM_SIZE} "--batch-ratio ${T_BATCH_RATIO} --max-batch-size ${T_MAX_BATCH_SIZE} --region-height ${T_REGION_HEIGHT}"
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
<<<<<<< HEAD
done
=======
done
>>>>>>> 6fe4270e5af4ec0184dc24ab62fc2ad3db10d956
