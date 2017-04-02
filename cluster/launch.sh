#!/usr/bin/env bash

CFG_COMMAND="-e JAVA_OPTS="'-Dhazelcast.config=/config/hazelcast.xml -DCLASSPATH=/dependencies'" -v /$(pwd)/config:/config -v /$(pwd)/../build/libs:/dependencies --net=host -P"

docker run -d --net=host -p 8080:8080 --name=hazelcast-management hazelcast/management-center
#sleep 10

docker run -d $CFG_COMMAND --name=hazelcast-member-1 kpts-hazelcast-member
docker run -d $CFG_COMMAND --name=hazelcast-member-2 kpts-hazelcast-member


# docker run -d --net=host -P --name=hazelcast-member kpts-hazelcast-member
