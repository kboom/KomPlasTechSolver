#!/usr/bin/env bash

CFG_COMMAND="-e JAVA_OPTS="'-Dhazelcast.config=/config/hazelcast.xml'" -v /$(pwd)/config:/config"

docker run -d -p 8080:8080 --name=hazelcast-management hazelcast/management-center
# let management center start up on time
sleep 10

# it is fine to expose only one member as they form the cluster and the client will know the others from the
# single member of the cluster this member is in
docker run -d $CFG_COMMAND -p 5701:5701 --name=hazelcast-member-1 kpts-hazelcast-member
docker run -d $CFG_COMMAND --name=hazelcast-member-2 kpts-hazelcast-member
