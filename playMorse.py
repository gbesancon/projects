#!/usr/bin/env python

import math
import time
import sys

import morse
import speakerphat

def playText( text ):
	morseCode = morse.convertTextToMorseCode(text)
	playMorseCode( morseCode )

def playMorseCode( morseCode ):
	for char in morseCode:
		if   char == morse.DOT:
			playDot()
			time.sleep(morse.TIME_FOR_DOT)
		elif char == morse.DASH:
			playDash()
			time.sleep(morse.TIME_FOR_DASH)
		elif char == morse.SPACE:
			playSpace()
			time.sleep(morse.TIME_FOR_SPACE)
	speakerphat.clear()

def playDot():
	speakerphat.clear()
	speakerphat.set_leds([0,0,0,0,255,255,0,0,0,0])
	speakerphat.show()

def playDash():
	speakerphat.clear()
	speakerphat.set_leds([255,255,255,255,255,255,255,255,255,255])
	speakerphat.show()

def playSpace():
	speakerphat.clear()

def main():
	text = sys.argv[1]
	playText( text )

if __name__ == "__main__":
	main()

