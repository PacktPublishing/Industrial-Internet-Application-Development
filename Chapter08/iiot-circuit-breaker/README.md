# demo-spring-boot-gateway [![Build Status](http://sjc1jenkins08.crd.ge.com/buildStatus/icon?job=CALM/Slot Tracker Service/slot-tracker-service-build)](http://sjc1jenkins08.crd.ge.com/job/CALM/job/Slot%20Tracker%20Service/job/slot-tracker-service-build/)
A service that does aggregation of data

### Building

``` shell
./build.sh
```

### Examine the MANIFEST

``` shell
unzip -p service/target/demo-spring-boot-gateway-1.0.0.LOCAL_BUILD.jar META-INF/MANIFEST.MF
```

### Run application
``` shell
java -jar service/target/demo-spring-boot-gateway-1.0.0.LOCAL_BUILD.jar 

Optionally add --server.port=nnnn if there is a port conflict
```

### API

curl -X GET -H "Content-Type: application/json" "http://localhost:8081/customer-assets"

```

### You can run tests in 2 modes
1) To run integration tests in Mock Web Application context
``` shell
java -jar test/target/demo-spring-boot-gateway-test-1.0.0.LOCAL_BUILD.jar
```
2) To run the same integration test on a deployed instance of service
``` shell
java -jar -Ddeployed.app.url.base=http://<hostname>:<port> test/target/demo-spring-boot-gateway-test-1.0.0.LOCAL_BUILD.jar
```
