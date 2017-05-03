#!/usr/bin/env bash
PUBLIC_ADDRESS=$(hostname -i)
JAVA_OPTS="$JAVA_OPTS -Dpublic.address=$PUBLIC_ADDRESS -Dmancenter.url=$MANCENTER_URL"

export JAVA_OPTS=${JAVA_OPTS} && bash server.sh