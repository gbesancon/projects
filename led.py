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
		if char == '.':
			displayDot()
		elif char == '-':
			displayDash()
		else:
			displaySpace()
		time.sleep(1)

def displayDot():
	speakerphat.clear()
	speakerphat.set_leds([0,0,0,0,255,255,0,0,0,0])
	speakerphat.show()

def displayDash():
	speakerphat.clear()
	speakerphat.set_leds([255,255,255,255,255,255,255,255,255,255])
	speakerphat.show()

def displaySpace():
	speakerphat.clear()

def main():
	text = sys.argv[1]
	displayText( text )

if __name__ == "__main__":
	main()

