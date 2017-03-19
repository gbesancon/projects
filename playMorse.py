#!/usr/bin/env python

import math
import time
import sys

import pygame

import morse
import speakerphatled

TIME_FOR_UNIT = 1.0 / 2.0

def outputText(text):
	morseCode = morse.convertTextToMorseCode(text)
	outputMorseCode( morseCode )

def outputMorseCode(morseCode):
	for morseCodeCharacter in morseCode:
		print morseCodeCharacter
		outputMorseCodeCharacter(morseCodeCharacter)

def outputMorseCodeCharacter(character):
	if   character == morse.DOT:
		outputDot()
	elif character == morse.DASH:
		outputDash()
	elif character == morse.LETTER_SEPARATOR:
		outputLetterSeparator()
	elif character == morse.WORD_SEPARATOR:
		outputWordSeparator()

def outputDot():
	speakerphatled.clear()
	speakerphatled.set_leds([0,0,0,0,255,255,0,0,0,0])
	speakerphatled.show()
	pygame.mixer.music.load('dot.wav')
	pygame.mixer.music.play()

def outputDash():
	speakerphatled.clear()
	speakerphatled.set_leds([255,255,255,255,255,255,255,255,255,255])
	speakerphatled.show()
	pygame.mixer.music.load('dash.wav')
	pygame.mixer.music.play()

def outputLetterSeparator():
	speakerphatled.clear()
	time.sleep(morse.LETTER_SEPARATOR_UNIT_MULTIPLIER)

def outputWordSeparator():
	speakerphatled.clear()
	time.sleep(morse.WORD_SEPARATOR_UNIT_MULTIPLIER)

def main():
	text = sys.argv[1]
	pygame.init()
	outputText(text)

if __name__ == "__main__":
	main()

