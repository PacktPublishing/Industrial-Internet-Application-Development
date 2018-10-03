export JAVA_HOME=${JAVA_HOME_ORACLEJDK8}
export PATH=${JAVA_HOME}/bin:${PATH}
. ./env.test_dev
#mvn clean install -s menlo_settings.xml -Dhttp.proxyHost=sjc1intproxy01.crd.ge.com -Dhttp.proxyPort=8080 -Dhttps.proxyHost=sjc1intproxy01.crd.ge.com -Dhttps.proxyPort=8080 -Dhttp.nonProxyHosts="*.ge.com|localhost"
mvn clean install 

