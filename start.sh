#!/bin/bash

cd /usr/src/app
python led.py &
aplay speaker-phat/test/test.wav &
npm start

