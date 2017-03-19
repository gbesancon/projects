#!/usr/bin/env python

import math
import time
import sys

import morse
import speakerphat

def displayText( text ):
	morseCode = morse.convertTextToMorseCode(text)
	displayMorseCode( morseCode )

def displayMorseCode( morseCode ):
	for char in morseCode:
		if   char == morse.DOT:
			displayDot()
		elif char == morse.DASH:
			displayDash()
		elif char == morse.SPACE:
			displaySpace()
	displaySpace()

def displayDot():
	speakerphat.clear()
	speakerphat.set_leds([0,0,0,0,255,255,0,0,0,0])
	speakerphat.show()
	time.sleep(morse.TIME_FOR_DOT)

def displayDash():
	speakerphat.clear()
	speakerphat.set_leds([255,255,255,255,255,255,255,255,255,255])
	speakerphat.show()
	time.sleep(TIME_FOR_DASH)

def displaySpace():
	speakerphat.clear()
	time.sleep(TIME_FOR_SPACE)

def main():
	text = sys.argv[1]
	displayText( text )

if __name__ == "__main__":
	main()

