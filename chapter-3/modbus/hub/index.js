var request = require('request');

var log = console.log;
//var mb = require('modbus').create(true); // enable debug output
var mb = require('modbus').create();

var sensor = 'REMOTE-SENSOR-ADDRESS';
var receiver = 'http://REMOTE-SERVER-ADDRESS.com:8080';

mb.onError(function (msg) {
  log('ERROR', msg);
});

// create master device
var ctx = mb.createMaster({

  // connection type and params
  con: mb.createConTcp(sensor, 1502),
  //con: mb.createConRtu(1, '/dev/ttyS1', 9600),

  // callback functions
  onConnect: function () {
    log('onConnect');
    log(ctx.getReg(2));
    ctx.setBit(1, false);

    //send to receiver
    var data = {
      device: 'sensor1',
      timestamp: Date.now(),
      reg2: ctx.getReg(2)
    };
    request.post({url: receiver, form: data}, function (err) {
      if (err) console.log('Failed to send to ' + receiver);
    });

    ctx.destroy();
  },
  onDestroy: function () {
    log('onDestroy');
  }
});
