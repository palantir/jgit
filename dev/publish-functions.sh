#!/usr/bin/env bash

set -euo pipefail

get_version() {
  git describe --tags --first-parent
}

set_version_and_package() {
  version=$(get_version)
  mvn -Dtycho.mode=maven org.eclipse.tycho:tycho-versions-plugin:set-version -DnewVersion="$version"
  mvn -f org.eclipse.jgit.packaging/pom.xml -Dtycho.mode=maven org.eclipse.tycho:tycho-versions-plugin:set-version -DnewVersion="$version"
  mvn -DskipTests package
  mvn -f org.eclipse.jgit.packaging/pom.xml -DskipTests package
}

publish_artifacts() {
  tmp_settings="tmp-settings.xml"
  echo "<settings><servers><server>" > $tmp_settings
  echo "<id>bintray-palantir-release</id><username>$BINTRAY_USERNAME</username>" >> $tmp_settings
  echo "<password>$BINTRAY_PASSWORD</password>" >> $tmp_settings
  echo "</server></servers></settings>" >> $tmp_settings

  mvn -T 1C --settings $tmp_settings -DskipTests deploy
}
