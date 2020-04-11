#!/usr/bin/env python

import sys
import os

#from espeak import espeak
# hanging in the is_playing loop
#def speak(text):
#	espeak.synth(text)
#	while espeak.is_playing:
#		pass

def speak(text):
	command = 'espeak "' + text + '"'
	os.system(command)

def main():
        text = sys.argv[1]
        print 'Text : ' + text
        speak(text)

if __name__ == "__main__":
        main()


