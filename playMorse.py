#!/usr/bin/env python

import math
import time
import sys

import pygame

import morse
import speakerphatled

TIME_FOR_UNIT = 1.0 / 3.0

def outputText(text):
	morseCode = morse.convertTextToMorseCode(text)
	outputMorseCode(morseCode)

def outputMorseCode(morseCode):
	for morseCodeCharacter in morseCode:
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
	pygame.mixer.music.load('dot.wav')
	speakerphatled.clear()
	speakerphatled.set_leds([0,0,0,0,255,255,0,0,0,0])
	speakerphatled.show()
	pygame.mixer.music.play()
	time.sleep(morse.DOT_UNIT_MULTIPLIER * TIME_FOR_UNIT)

def outputDash():
	pygame.mixer.music.load('dash.wav')
	speakerphatled.clear()
	speakerphatled.set_leds([255,255,255,255,255,255,255,255,255,255])
	speakerphatled.show()
	pygame.mixer.music.play()
	time.sleep(morse.DASH_UNIT_MULTIPLIER * TIME_FOR_UNIT)

def outputLetterSeparator():
	speakerphatled.clear()
	time.sleep(morse.LETTER_SEPARATOR_UNIT_MULTIPLIER * TIME_FOR_UNIT)

def outputWordSeparator():
	speakerphatled.clear()
	time.sleep(morse.WORD_SEPARATOR_UNIT_MULTIPLIER * TIME_FOR_UNIT)

def main():
	text = sys.argv[1]
	pygame.init()
	outputText(text)

if __name__ == "__main__":
	main()

