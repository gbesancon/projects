#!/usr/bin/env python

import math
import time
import sys

import pygame

import morse
import speakerphat-led

def outputText(text):
	morseCode = morse.convertTextToMorseCode(text)
	outputMorseCode( morseCode )

def outputMorseCode(morseCode):
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

def outputMorseCodeCharacter(character):
	if   char == morse.DOT:
		outputDot()
		time.sleep(morse.TIME_FOR_DOT)
	elif char == morse.DASH:
		outputDash()
		time.sleep(morse.TIME_FOR_DASH)
	elif char == morse.LETTER_SEPARATOR:
		outputLetterSeparator()
		time.sleep(morse.TIME_FOR_LETTER_SEPARATOR)
	elif char == morse.WORD_SEPARATOR:
		outputWordSeparator()
		time.sleep(morse.TIME_FOR_WORD_SEPARATOR)

def outputDot():
	speakerphat-led.clear()
	speakerphat-led.set_leds([0,0,0,0,255,255,0,0,0,0])
	speakerphat-led.show()
	pygame.mixer.music.load('dot.wav')
	pygame.mixer.music.play()

def outputDash():
	speakerphat-led.clear()
	speakerphat-led.set_leds([255,255,255,255,255,255,255,255,255,255])
	speakerphat-led.show()
	pygame.mixer.music.load('dash.wav')
	pygame.mixer.music.play()

def outputLetterSeparator():
	speakerphat-led.clear()

def outputWordSeparator():
	speakerphat-led.clear()

def main():
	text = sys.argv[1]
	outputText(text)

if __name__ == "__main__":
	main()

