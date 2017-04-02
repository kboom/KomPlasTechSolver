#!/usr/bin/env bash

if [ $(which docker | wc -c) -eq 0 ]
then
echo "You do not have docker installed. Download it"
exit 1
fi

if [ "$(docker-machine status)" == "Stopped" ]
then
docker-machine start
fi

eval "$(docker-machine env)"

docker build -t kpts-hazelcast-member .
docker pull hazelcast/management-center:latest