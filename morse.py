import sys

CODE = {
	'A': '.-',     'B': '-...',   'C': '-.-.', 
        'D': '-..',    'E': '.',      'F': '..-.',
        'G': '--.',    'H': '....',   'I': '..',
        'J': '.---',   'K': '-.-',    'L': '.-..',
        'M': '--',     'N': '-.',     'O': '---',
        'P': '.--.',   'Q': '--.-',   'R': '.-.',
     	'S': '...',    'T': '-',      'U': '..-',
        'V': '...-',   'W': '.--',    'X': '-..-',
        'Y': '-.--',   'Z': '--..',

        ' ': '/',

        '0': '-----',  '1': '.----',  '2': '..---',
        '3': '...--',  '4': '....-',  '5': '.....',
        '6': '-....',  '7': '--...',  '8': '---..',
        '9': '----.', 

        '.': '.-.-.-', ',': '--..--', '?': '..--..',
        '\'': '.----.', '!': '-.-.--', '/': '-..-.',
        '(': '-.--.', ')': '-.--.-', '&': '.-...',
        ':': '---...', ';': '-.-.-.', '=': '-...-',
        '+': '.-.-.-', '-': '-....-', '_': '..--.-',
        '"': '.-..-.', '$': '...-..-', '@': '.--.-.' 
        }

TIME_FOR_UNIT = 1.0 / 3.0

DOT = '.'
TIME_FOR_DOT = 1.0 * TIME_FOR_UNIT
DASH = '-'
TIME_FOR_DASH = 3.0 * TIME_FOR_UNIT
LETTER_SEPARATOR = ' '
TIME_FOR_LETTER_SEPARATOR = 1.0 * TIME_FOR_UNIT
WORD_SEPARATOR = '/'
TIME_FOR_WORD_SEPARATOR = 7.0 * TIME_FOR_UNIT

def convertTextToMorseCode( text ):
	morseCode = ''
	for char in text:
		morseCode = morseCode + CODE[char.upper()]
		morseCode = morseCode + LETTER_SEPARATOR 
	return morseCode

def main():
	text = sys.argv[1]
	print 'Text      : ' + text
	morseCode = convertTextToMorseCode(text)
	print 'MORSE Code: ' + morseCode
		
if __name__ == "__main__":
	main()
