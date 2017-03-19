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

        ' ': ' ',

        '0': '-----',  '1': '.----',  '2': '..---',
        '3': '...--',  '4': '....-',  '5': '.....',
        '6': '-....',  '7': '--...',  '8': '---..',
        '9': '----.' 
        }

def convertTextToMorseCode( text ):
	morseCode = ''
	for char in text:
		morseCode = morseCode + CODE[char.upper()]
	return morseCode

def main():
	text = sys.argv[1]
	print 'Text      : ' + text
	morseCode = convertTextToMorseCode(text)
	print 'MORSE Code: ' + morseCode
		
if __name__ == "__main__":
	main()
