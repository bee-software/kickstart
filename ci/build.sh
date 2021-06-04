#!/bin/bash

export TERM=${TERM:-dumb}
cd sources
./gradlew build
