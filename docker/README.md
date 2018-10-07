# Running the computations

## Cluster mode


## Local mode (debug)
In order to run the worker

``` bash
docker run -it -e JAVA_OPTS="-Djoin.interface=localhost -Dexecutor.pool.size=8 -Dpublic.address=localhost" -p 5701:5701 kpts-worker-local
```

In order to run the comptations do
``` bash
docker run -it -e CLUSTER_ADDRESS=192.168.1.25 kbhit/kpts-solver-performance
```