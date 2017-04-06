#!/usr/bin/env bash
kinit

counter=0
function restart_node {
    node_name="node$1"
    counter=$((counter+1))
    ssh ${node_name} 'kill -9 $(/usr/sbin/lsof -t -i:5701) && ./solverCommands/restart-node.sh '${counter}'' &
}

restart_node 2
restart_node 3
restart_node 4
restart_node 5
restart_node 9