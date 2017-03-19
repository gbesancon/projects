#!/usr/bin/env python

import math
import time
import sys

import morse
import speakerphat-audio
import speakerphat-led

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
		elif char == morse.LETTER_SEPARATOR:
			playLetterSeparator()
			time.sleep(morse.TIME_FOR_LETTER_SEPARATOR)
		elif char == morse.WORD_SEPARATOR:
			playWordSeparator()
			time.sleep(morse.TIME_FOR_WORD_SEPARATOR)
	speakerphat-led.clear()

def playDot():
	speakerphat-led.clear()
	speakerphat-led.set_leds([0,0,0,0,255,255,0,0,0,0])
	speakerphat-led.show()

def playDash():
	speakerphat-led.clear()
	speakerphat-led.set_leds([255,255,255,255,255,255,255,255,255,255])
	speakerphat-led.show()

def playLetterSeparator():
	speakerphat-led.clear()

def playWordSeparator():
	speakerphat-led.clear()

def main():
	text = sys.argv[1]
	playText( text )

if __name__ == "__main__":
	main()

