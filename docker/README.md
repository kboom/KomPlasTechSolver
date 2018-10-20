# Running the computations

## Cluster mode


## Local mode (debug)
In order to run the worker

``` bash
docker run -it -e JAVA_OPTS="-Xmx2G -Xms2G -Djoin.interface=localhost -Dexecutor.pool.size=8 -Dpublic.address=localhost" -p 5701:5701 kpts-worker-local
```

In order to run the comptations do
``` bash
docker run -it -v $(pwd)/logs:/home/app/logs -e CLUSTER_ADDRESS=192.168.1.25 -e CUSTOM_JAVA_OPTS="-Xmx4G -Xms4G -DCOMPUTATIONS_TIME_LOG_FILENAME=/home/app/logs/test.log" kbhit/kpts-solver-performance
```

Get container revisions
``` bash
kubectl get pod iga-job-88cjq -o json | jq '.status.containerStatuses[] | { "image": .image, "imageID": .imageID }'
```