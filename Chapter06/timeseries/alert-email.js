var email = require('emailjs');

var server = email.server.connect({
  user: 'username@gmail.com',
  password: 'password',
  host: 'smtp.gmail.com',
  ssl: true
});

// send the message and get a callback with an error or details of the message that was sent
function alertEmail(msg) {
  server.send({
    text: msg,
    from: 'username@gmail.com',
    to: 'username@gmail.com',
    subject: 'Alert'
  }, function (err, message) {
    console.log(err || message);
  });
}

module.exports = alertEmail;
