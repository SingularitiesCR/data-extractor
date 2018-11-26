#!/usr/bin/env bash

readonly CI_COMMIT_ID=${CI_COMMIT_ID}

mvn -q versions:set -DnewVersion=${CI_COMMIT_ID} && \
    mvn -q versions:commit && \
    mvn -q deploy && \
    echo "deployed service-apis version ${CI_COMMIT_ID}"
