#!/usr/bin/env python

import math
import time
import sys

import speakerphat

for i in range(1000):
    offset = 0
    speakerphat.clear()
    speakerphat.set_led(offset,255)
    speakerphat.show()

