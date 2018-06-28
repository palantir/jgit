#!/usr/bin/env bash

set -euo pipefail

FWDIR="$(cd "`dirname "${BASH_SOURCE[0]}"`"; pwd)"

source "$FWDIR/publish-functions.sh"

set_version_and_package
publish_artifacts | tee -a "/tmp/publish_artifacts.log"
