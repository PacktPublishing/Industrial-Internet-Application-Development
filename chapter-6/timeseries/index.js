var restify = require('restify');
var server = restify.createServer({name: 'MyApp'});
var Influx = require('influx');
var alertEmail = require('./alert-email');
var slack = require('slack-incoming-webhook')({url: '{webhook url}', });

var min = 0;
var max = 100;
var alertFrom = 90;
var interval = 1000;
var alertSlack = slack({
  url: 'https://hooks.slack.com/services/xxx/yyy/zzz',
  channel: '#channel-name',
  icon: ':warning:'
});

// create connection
const influx = new Influx.InfluxDB({
  host: 'localhost',
  database: 'mydb',
  schema: [
    {
      measurement: 'sensor1',
      fields: {
        variable1: Influx.FieldType.INTEGER
      },
      tags: [
        'device'
      ]
    }
  ]
});

// simulate receiving data from sensors
setInterval(function () {
  var variable1 = Math.floor(max - Math.random() * (max - min));

  if (variable1 >= alertFrom) {
    var msg = 'variable exceeded ' + variable1 + ' rpm';
    alertEmail(msg);
    alertSlack(msg);
  }

  influx.writePoints([
    {
      measurement: 'sensor1',
      tags: {device: 'raspberry'},
      fields: {variable1: variable1}
    }
  ]).then(function () {
    console.log(variable1);
  }).catch(function (err) {
    console.error(err);
  });
}, interval);

server.use(function search(req, res, next) {
  res.header('Access-Control-Allow-Origin', '*');
  res.header('Access-Control-Allow-Headers', 'X-Requested-With');
  return next();
});

server.get('/last', function search(req, res, next) {
  influx.query('select * from sensor1 WHERE time > now() - 5m').then(function (result) {
    res.json(result)
  }).catch(function (err) {
    res.status(500).send(err.stack)
  });
  return next();
});

server.get('/stats', function search(req, res, next) {
  influx.query('SELECT COUNT(*), MEAN(*), MEDIAN(*), MODE(*), SPREAD(*), STDDEV(*), SUM(*), FIRST(*), LAST(*), MAX(*), MIN(*), PERCENTILE(*, 5) FROM "sensor1" WHERE time > now() - 1h').then(function (result) {
    res.json(result)
  }).catch(function (err) {
    res.status(500).send(err.stack)
  });
  return next();
});

server.get(/\/public\/?.*/, restify.plugins.serveStatic({
  directory: __dirname
}));

server.listen(8080);
