#!/bin/bash

export TERM=${TERM:-dumb}
cd sources
./gradlew --no-daemon build
