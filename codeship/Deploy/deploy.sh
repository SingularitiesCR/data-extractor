#!/usr/bin/env bash

readonly CI_COMMIT_ID=${CI_COMMIT_ID}

mvn -q versions:set -DnewVersion=${CI_COMMIT_ID} && \
    mvn -q versions:commit && \
    mvn -q deploy -Dmaven.test.skip=true && \
    echo "deployed service-apis version ${CI_COMMIT_ID}"
