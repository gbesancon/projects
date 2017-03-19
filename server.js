var express = require('express');
var app = express();
var request = require('request');
var sound = require('node-aplay');
var child_process = require('child_process');

app.get('/', function (req, res) {	 
	res.send('');
});

app.get('/morse', function (req, res) {	 
	child_process.execFile('python', ['/usr/src/app/playMorse.py', 'sos'],
		(error, stdout, stderr) => {
			if (error !== null) {
				throw error;
			}
			console.log('stdout: ' + stdout);
			console.log('stderr: ' + stderr);
		});
	res.send('MORSE !!!');
});

// start a server on port 80 and log its start to our console
var server = app.listen(80, function () {
  var port = server.address().port;
});

