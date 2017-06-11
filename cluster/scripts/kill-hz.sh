#!/usr/bin/env bash
HZ_PID=$(lsof -i :5701 | awk 'FNR == 2 {print $2}')
kill -9 ${HZ_PID}