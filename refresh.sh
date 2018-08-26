#!/usr/bin/env bash

kubectl patch deployment iga -p \
  "{\"spec\":{\"template\":{\"metadata\":{\"labels\":{\"date\":\"`date +'%s'`\"}}}}}"