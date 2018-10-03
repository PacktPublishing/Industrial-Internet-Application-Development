# demo-api-gateway
The sample application that uses Microservices communication pattern: API Gateway

### Building
```
mvn clean install
```

### Deploy the application in your local machine
1) Run application in your local machine
```
java -jar ./target/demo-api-gateway-1.0.0.jar
```

2) Access application started in your local machine through web browser
```
http://localhost:8080/gateway/v1/shop?name=USA

http://localhost:8080/gateway/v1/shops
```

### Deploy application to Cloud Foundry
1) Push application to Cloud Foundry
```
cf push
```

2) Access application through web browser
```
https://demo-api-gateway.run.asv-pr.ice.predix.io/gateway/v1/shop?name=USA

https://demo-api-gateway.run.asv-pr.ice.predix.io/gateway/v1/shops
```

