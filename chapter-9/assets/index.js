var WebSocketClient = require('websocket').client;
var uaaUtil = require('predix-uaa-client');
var url = require('url');

var tsUrl = 'wss://gateway-predix-data-services.run.aws-usw02-pr.ice.predix.io/v1/stream/messages';
var uaaUrl = 'https://e0fd8047-0a1d-4076-9143-fc4cbf60cd79.predix-uaa.run.aws-usw02-pr.ice.predix.io/oauth/token';
var clientId = 'client name';
var clientSecret = 'client secret';
var min = 0;
var max = 100;
var interval = 1000;

uaaUtil.getToken(uaaUrl, clientId, clientSecret).then(function (token) {
  var headers = {
    Authorization: 'Bearer ' + token.access_token,
    'predix-zone-id': '6f6882ff-3a3b-45b2-b20b-b537e652471d',
    'Origin': 'http://localhost'
  };
  console.log('headers: ', headers);

  var requestOptions = {agent: false};

  var client = new WebSocketClient();

  client.on('connectFailed', function (error) {
    console.log('connection error: ', error);
  });

  client.on('connect', function (connection) {
    console.log('connected to: ', tsUrl);

    connection.on('error', function (error) {
      console.log('connection error: ', error);
    });

    connection.on('close', function () {
      console.log('connection closed');
    });
    connection.on('message', function (message) {
      if (message.type === 'utf8') {
        console.log('response: ', message.utf8Data);
      }
    });

    setInterval(function () {
      var variable1 = Math.floor(max - Math.random() * (max - min));

      var data = JSON.stringify({
        messageId: Date.now(),
        body: [
          {
            name: 'sensor1:variable1',
            datapoints: [
              [Date.now(), variable1]
            ],
            attributes: {
              devices: '/device/raspberry'
            }
          }
        ]
      });
      console.log('sending: ', data);
      connection.sendUTF(data);
    }, interval);
  });

  client.connect(tsUrl, null, 'http://localhost', headers, requestOptions);
}).catch(function (err) {
  console.error('failed to get token', err);
});
