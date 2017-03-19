# Updated from: 
# git clone https://github.com/pimoroni/speaker-phat.git
# speaker-phat/python/speakerphat.py

import atexit
from sys import exit

try:
    import sn3218
except ImportError:
    exit("This library requires the sn3218 module\nInstall with: sudo pip install sn3218")


stupid_led_mappings = [0, 1, 2, 4, 6, 8, 10, 12, 14, 16]

led_values = [0 for x in range(18)]
enable_leds = 0

WIDTH = 10
HEIGHT = 1

for x in stupid_led_mappings:
    enable_leds |= 1 << x

sn3218.enable_leds(enable_leds)
sn3218.enable()

def clear():
    global led_values
    led_values = [0 for x in range(18)]
    show()

def set_led(index, value):
    led_values[stupid_led_mappings[index]] = value

def set_leds(values):
    for index in range(len(values)):
    	set_led(index, values[index]) 

def show():
    sn3218.output(led_values)

atexit.register(clear)
