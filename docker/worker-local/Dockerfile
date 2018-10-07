FROM hazelcast/hazelcast:latest

COPY hazelcast.xml $HZ_HOME
COPY *.jar /opt/libs
ENV CLASSPATH /opt/libs
# ENV JAVA_OPTS -Dexecutor.pool.size=$EXECUTOR_POOL_SIZE -Djoin.interface=$JOIN_INTERFACE -Dpublic.address=$PUBLIC_ADDRESS -Dmancenter.url=$MANCENTER_URL