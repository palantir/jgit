#!/usr/bin/env bash

set -euo pipefail

get_version() {
  git describe --tags --first-parent
}

set_version() {
  version=$(get_version)
  mvn -f pom.xml -Dtycho.mode=maven tycho-version:set-version -DnewVersion="$version"
  mvn -f org.eclipse.jgit.packaging/pom.xml -Dtycho.mode=maven tycho-version:set-version -DnewVersion="$version"
}

publish_artifacts() {
  tmp_settings="tmp-settings.xml"
  echo "<settings><servers><server>" > $tmp_settings
  echo "<id>bintray-palantir-release</id><username>$BINTRAY_USERNAME</username>" >> $tmp_settings
  echo "<password>$BINTRAY_PASSWORD</password>" >> $tmp_settings
  echo "</server></servers></settings>" >> $tmp_settings

  mvn -f pom.xml -s $tmp_settings -DskipTests install deploy:deploy -DretryFailedDeploymentCount=3
  mvn -f org.eclipse.jgit.packaging/pom.xml -s $tmp_settings clean install deploy:deploy -DretryFailedDeploymentCount=3
}
