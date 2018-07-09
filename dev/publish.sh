#!/usr/bin/env bash

set -euo pipefail

FWDIR="$(cd "`dirname "${BASH_SOURCE[0]}"`"; pwd)"

source "$FWDIR/publish-functions.sh"

JAVA_HOME=/usr/lib/jvm/jdk1.8.0 set_version
publish_artifacts | tee -a "/tmp/publish_artifacts.log"
