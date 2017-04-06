#!/usr/bin/env bash
kinit

counter=0
function restart_node {
    node_name="node$1"
    counter=$((counter+1))
    cmd='kill -9 $(/usr/sbin/lsof -t -i:5701) &> /dev/null && ./solverCommands/restart-node.sh '${counter}''
    echo "Running ${cmd}"
    ssh ${node_name} ${cmd} &
}

restart_node 2
restart_node 3
restart_node 4
restart_node 5
restart_node 9