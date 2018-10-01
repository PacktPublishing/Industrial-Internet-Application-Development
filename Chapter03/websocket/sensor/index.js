var WebSocket = require('ws');
var rpio = require('rpio');

var ws;
var receiver = 'ws://REMOTE-SERVER-ADDRESS.com:8080';
rpio.open(11, rpio.INPUT);

var establishConnection = function () {
  ws = new WebSocket(receiver);
  ws.on('close', establishConnection);
  ws.on('error', establishConnection);
};
establishConnection();

var sendStatus = function () {
  var status = rpio.read(11) === 0;
  console.log('light status: ' + status);

  var data = JSON.stringify({
    device: 'raspberry',
    timestamp: Date.now(),
    light: status
  });

  try { ws.send(data); }
  catch (e) { console.log('failed to send data to ' + receiver); }

  setTimeout(sendStatus, 1000);
};
sendStatus();
