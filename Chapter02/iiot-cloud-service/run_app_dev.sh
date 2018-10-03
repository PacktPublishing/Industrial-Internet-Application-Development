#!/bin/sh

. ./env.test_dev
#export VCAP_SERVICES=$VCAP_SERVICES_SMOKE
cd iiot-sample 
#mvn spring-boot:run -Drun.jvmArguments="-Dhttp.proxyHost=sjc1intproxy01.crd.ge.com -Dhttp.proxyPort=8080 -Dhttps.proxyHost=sjc1intproxy01.crd.ge.com -Dhttps.proxyPort=8080 -Dhttp.nonProxyHosts=*.ge.com|localhost" -s ../menlo_settings.xml
mvn spring-boot:run -Drun.jvmArguments="-Dhttp.proxyHost=sjc1intproxy01.crd.ge.com -Dhttp.proxyPort=8080 -Dhttps.proxyHost=sjc1intproxy01.crd.ge.com -Dhttps.proxyPort=8080 -Dhttp.nonProxyHosts=*.ge.com|localhost"
cd ..
