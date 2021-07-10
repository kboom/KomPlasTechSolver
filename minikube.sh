#!/usr/bin/env bash

set -ex

minikube config set cpus 4
minikube config set memory 4096
minikube config view
minikube delete || true
minikube start --vm-driver=hyperkit