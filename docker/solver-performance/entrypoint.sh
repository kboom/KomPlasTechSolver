#!/usr/bin/env sh
set -euxo pipefail

# Check required environment variables
if [ -z "${CLUSTER_ADDRESS-}" ];
    then echo "CLUSTER_ADDRESS must be set";
    exit 1;
fi

PROBLEM_SIZES=${PROBLEM_SIZES:-"12 24 48 96 192 384 768 1536 3072"}
REGION_HEIGHTS=${REGION_HEIGHTS:-"1 2 3 4 5 6 7 8"}
BATCH_RATIOS=${BATCH_RATIOS:-"4"}
MAX_BATCH_SIZES=${MAX_BATCH_SIZES:-"10"}
COOLDOWN=5

JVM_OPTS="-XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -XX:MaxRAMFraction=1 -XshowSettings:vm"
PROGRAM_JVM_OPTS="-Dcluster.address=${CLUSTER_ADDRESS}"
JAR_LOCATION="*.jar"
CMD="${JVM_OPTS} ${PROGRAM_JVM_OPTS} -jar ${JAR_LOCATION} ${@}"

for problemSize in $PROBLEM_SIZES
do
    for regionHeight in $REGION_HEIGHTS
    do
        for batchRatio in $BATCH_RATIOS
        do
            for maxBatchSize in $MAX_BATCH_SIZES
            do
                echo "Running for $problemSize / $regionHeight / $batchRatio / $maxBatchSize\n"
                java ${CMD} --problem-size $problemSize --region-height $regionHeight --batch-ratio $batchRatio --max-batch-size $maxBatchSize || true
                sleep $COOLDOWN
            done
        done
    done
done
