#!/usr/bin/env bash

readonly CI_COMMIT_ID=${CI_COMMIT_ID}

mvn versions:set -q -DnewVersion=${CI_COMMIT_ID} && \
    mvn versions:commit -q && \
    mvn deploy -q -Dmaven.test.skip=true
