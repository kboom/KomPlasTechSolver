#!/usr/bin/env bash
docker run -it -e JAVA_OPTS="-Xms14G -Xmx14G -Dcluster.address=10.0.0.2-10" --net=hazelcast-net -v /home/ubuntu/workdir:/workdir kbhit/kpts-solver --problem-size $1 --steps 1 $2