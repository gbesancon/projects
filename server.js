var express = require('express');
var app = express();
var sound = require('node-aplay');
var request = require('request');

app.get('/', function (req, res) {	 
	res.send('');
});

app.get('/play', function (req, res) {	 
	new sound('/usr/src/app/speaker-phat/test/test.wav').play();
	res.send('Music !!!');
});

//start a server on port 80 and log its start to our console
var server = app.listen(80, function () {
  var port = server.address().port;
});

