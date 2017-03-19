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
		if char == DOT:
			displayDot()
		elif char == DASH:
			displayDash()
		elif char == SPACE:
			displaySpace()
	displaySpace()

def displayDot():
	speakerphat.clear()
	speakerphat.set_leds([0,0,0,0,255,255,0,0,0,0])
	speakerphat.show()
	time.sleep(TIME_FOR_UNIT)

def displayDash():
	speakerphat.clear()
	speakerphat.set_leds([255,255,255,255,255,255,255,255,255,255])
	speakerphat.show()
	time.sleep(3*TIME_FOR_UNIT)

def displaySpace():
	speakerphat.clear()
	time.sleep(7*TIME_FOR_UNIT)

def main():
	text = sys.argv[1]
	displayText( text )

if __name__ == "__main__":
	main()

