var express = require('express');
var app = express();
var request = require('request');
var sound = require('node-aplay');
var exec = require('exec');

app.get('/', function (req, res) {	 
	res.send('');
});

app.get('/music', function (req, res) {	 
	new sound('/usr/src/app/speaker-phat/test/test.wav').play();
	res.send('Music !!!');
});

app.get('/led', function (req, res) {	 
	exec('python /usr/src/app/led.py',
		function (error, stdout, stderr) {
			console.log('stdout: ' + stdout);
			console.log('stderr: ' + stderr);
			if (error !== null) {
				console.log('exec error: ' + error);
			}
		});
	res.send('LED !!!');
});

// start a server on port 80 and log its start to our console
var server = app.listen(80, function () {
  var port = server.address().port;
});

