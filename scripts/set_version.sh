#!/bin/bash

new_version=$1
# ------------------------------------------
if [[ "$new_version" == "" ]]; then
  echo "Give a version to set (e.g., 1.0.0)"
  exit 1
fi

find . -name pom.xml | xargs gsed -i "s/<version>[0-9]\+\.[0-9]\+\.[^S]\+<\/version>/<version>${new_version}<\/version>/g"
find . -name pom.xml | xargs gsed -i "s/<version>[0-9]\+\.[0-9]\+\.[^S]\+-SNAPSHOT<\/version>/<version>${new_version}<\/version>/g"

if [[ "$new_version" != *-SNAPSHOT ]]; then
  gsed -i "s/<deepl-sdk.version>[0-9]\+\.[0-9]\+\.[^S]\+<\/deepl-sdk.version>/<deepl-sdk.version>${new_version}<\/deepl-sdk.version>/g" README.md
  gsed -i "s/DeepLSDKVersion = \"[0-9]\+\.[0-9]\+\.[^S]\+\"/DeepLSDKVersion = \"${new_version}\"/g" README.md
fi

echo "package deepl.api

object Metadata {
  const val VERSION: String = \"$new_version\"

  fun isLibraryMaintainerMode(): Boolean {
    val value = System.getenv(\"DEEPL_SDK_JVM_LIBRARY_MAINTAINER_MODE\")
    return value != null && ((value == \"1\") or (value == \"true\"))
  }
}" > core/src/main/kotlin/deepl/api/Metadata.kt

git diff