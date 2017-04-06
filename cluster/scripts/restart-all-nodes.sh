#!/usr/bin/env bash
kinit

counter=0
function restart_node {
    node_name=$(eval "node$1")
    counter++
    ssh ${node_name} 'kill -9 $(/usr/sbin/lsof -t -i:5701) && ./restart-node.sh '${counter}'' &
}

restart_node 2
restart_node 3
restart_node 4
restart_node 5
restart_node 9