var http = require('http');
var querystring = require('querystring');
var Pool = require('pg').Pool;
var pool = new Pool({
  user: 'postgres',
  database: 'iot-book',
  password: 'password',
  host: 'host',
  port: 5433
});

//ensure table exists in db
pool.query('CREATE TABLE IF NOT EXISTS "sensor-logs" (id serial NOT NULL PRIMARY KEY, data json NOT NULL)', function (err, result) {
  if (err) console.log(err);
});

http.createServer(function (req, res) {
  req.on('data', function (chunk) {
    var data = querystring.parse(chunk.toString());
    console.log(data);

    //save in db
    pool.query('INSERT INTO "sensor-logs" (data) VALUES ($1)', [data], function (err, result) {
      if (err) console.log(err);
    });
  });
  req.on('end', function () {
    res.writeHead(200, 'OK', {'Content-Type': 'text/html'});
    res.end('ok')
  });
}).listen(process.env.PORT || 8080);
