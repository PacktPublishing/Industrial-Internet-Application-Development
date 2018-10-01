const WebSocket = require('ws');

const wss = new WebSocket.Server({port: 8080}, function () {
  console.log('Websocket server started');
});

wss.on('connection', function connection(ws) {
  ws.on('message', function incoming(message) {
    console.log('received: ', message);
  });

  // Send message to connected client
  ws.send('hello, client');
});
