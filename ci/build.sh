#!/bin/bash

export TERM=${TERM:-dumb}
cd sources
gradle --no-daemon build