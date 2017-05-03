#!/usr/bin/env bash
kinit

counter=0
function destroy_node {
    node_name="node$1"
    counter=$((counter+1))
    cmd='kill -9 $(/usr/sbin/lsof -t -i:5701 -i:5702 -i:5703) &> /dev/null ;'
    echo "Running ${cmd}"
    ssh ${node_name} ${cmd}
}

destroy_node 2
destroy_node 3
destroy_node 4
destroy_node 5
destroy_node 9
