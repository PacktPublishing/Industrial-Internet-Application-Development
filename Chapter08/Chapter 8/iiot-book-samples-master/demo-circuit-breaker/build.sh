#!/bin/bash

ci_build_number="LOCAL_BUILD"

if [ -n "$BUILD_NUMBER" ]; then
    ci_build_number=$BUILD_NUMBER
fi

mvn clean package -Dci.build.number=$ci_build_number -s mvn_settings.xml
