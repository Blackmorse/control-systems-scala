#!/bin/bash
cd $(dirname "$0")

sbt "run -Dconfig.file=$(dirname "$0")/conf/application-prod.conf"
