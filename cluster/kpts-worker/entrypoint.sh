#!/usr/bin/env bash
PUBLIC_ADDRESS=$(hostname -i)
JAVA_OPTS="-Dpublic.address=$PUBLIC_ADDRESS"

export JAVA_OPTS=${JAVA_OPTS} && bash server.sh