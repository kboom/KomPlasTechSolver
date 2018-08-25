#!/usr/bin/env sh
set -euxo pipefail

# Check required environment variables
if [ -z "${CLUSTER_ADDRESS-}" ];
    then echo "CLUSTER_ADDRESS must be set";
    exit 1;
fi

JVM_OPTS="-XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -XX:MaxRAMFraction=2 -XshowSettings:vm"
PROGRAM_JVM_OPTS="-Dcluster.address=${CLUSTER_ADDRESS}"
JAR_LOCATION="*.jar"

CMD="${JVM_OPTS} ${PROGRAM_JVM_OPTS} -jar ${JAR_LOCATION} ${@}"

echo "Executing command java $CMD"
java ${CMD}
