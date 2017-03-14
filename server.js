var express = require('express');
var app = express();
var Sound = require('node-aplay');
var request = require('request');
var myIP = require('my-ip');

app.get('/play', function (req, res) {	 
	// fire and forget: 
	new Sound('/usr/src/app/speaker-phat/test/test.wav).play();
	res.send('Music!!!');
});

//start a server on port 80 and log its start to our console
var server = app.listen(80, function () {
  var port = server.address().port;
});
