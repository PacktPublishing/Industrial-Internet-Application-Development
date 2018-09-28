var restify = require('restify');
var server = restify.createServer({name: 'MyApp'});
server.use(restify.bodyParser());

var Pool = require('pg').Pool;
var pool = new Pool({
  database: 'iot-book',
  host: 'host',
  password: 'password',
  port: 5433,
  user: 'postgres',
});

//ensure table exists in db
pool.query('CREATE TABLE IF NOT EXISTS "sensor-logs" (id serial NOT NULL PRIMARY KEY, data json NOT NULL)', function (err, result) {
  if (err) console.log(err);
});

server.post('/', function create(req, res, next) {
  console.log(req.params);

  //save in db
  pool.query('INSERT INTO "sensor-logs" (data) VALUES ($1)', [req.params], function (err, result) {
    if (err) console.log(err);
    res.send(201, result);
  });

  return next();
});

server.get('/stats', function search(req, res, next) {
  pool.query('SELECT AVG("Variable1"), MAX("Variable1"), MIN("Variable1"), COUNT(*), SUM("Variable1") FROM (SELECT (data->>\'Variable1\')::int "Variable1" FROM "sensor-logs" ORDER BY id DESC LIMIT 10) data', function (err, result) {
    if (err) console.log(err);
    res.send(result.rows);
  });
  return next();
});

server.listen(process.env.PORT || 8080);
