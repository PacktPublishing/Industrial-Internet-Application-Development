
var Stomp = require('webstomp-client');
var SockJS = require('sockjs-client');
var csv=require('fast-csv'),
 	fs = require("fs");
   

var socket = new SockJS('http://localhost:5074/iiot-sample-websocket');
var stompClient = Stomp.over(socket);





function readFromSensorDataAndCheckforAnomalies(){

	var stream = fs.createReadStream("sensordata.csv");
 
	csv
	 .fromStream(stream, {headers : true})
	 .validate(function(data){
	     return data.temp <= 50; //all persons must be under the age of 50 
	 })
	 .on("data-invalid", function(data){
	      

	      var alertData = JSON.stringify({'alertsUuid':'alertsid'+ Date.now()+data.temp ,
											'severity':1,
											'alertName':'HighTemprateAlert',
											'alertInfo':'Temperature Value:' + data.temp });
		   stompClient.send("/app/createAlert",alertData,{});
	 })
	 .on("data", function(data){
	     console.log(data);
	 })
	 .on("end", function(){
	     console.log("done");
	 });
}


function createAlertInCloud() {

	// Connect to cloud using websocket and create alerts
  
    stompClient.connect({}, function (frame) {
        //setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/alertCreated', function (alert) {
             console.log('gotResponse from CloudService: ' + alert);
        });
        readFromSensorDataAndCheckforAnomalies();
        
    });
};


createAlertInCloud();



//createAlertInCloud();




