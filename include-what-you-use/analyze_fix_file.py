# https://code.google.com/p/include-what-you-use/
# http://softwarerecs.stackexchange.com/questions/4461/automate-include-refactoring-in-c
# http://codedammit.com/remove-unnecessary-include-with-include-what-you-use-under-windows/
# http://blog.slaks.net/2013-10-18/extending-visual-studio-part-1-getting-started/
# http://www.viva64.com/en/a/0082/

Python27=r"C:\Python27\python.exe"

def AnalyzeAndFixFile(filePath, includeDirectoryFilePath, iwyuOptionsFilePath):
    import os
    from os.path import join, relpath
    import analyze_file
    fileName=os.path.basename(os.path.normpath(filePath))
    logFileName=fileName + ".txt"
    logFilePath=join(os.getcwd(), logFileName)
    analyze_file.AnalyzeFile(filePath, includeDirectoryFilePath, iwyuOptionsFilePath, True, logFilePath)
    os.system(Python27 + " fix_includes.py < " + logFilePath)
    os.remove(logFileName)
    
def main(argv):
    import sys, getopt
    filePath = ''
    includeDirectoriesFilePath = ''
    iwyuOptionsFilePath = ''
    logDirectoryPath = ''

    def printUsage():
        print('analyze_fix_file.py -f <filePath> -i <includeDirectoriesFilePath> -o <iwyuOptionsFilePath>')

    try:
        opts, args = getopt.getopt(argv,"hf:i:o:")
    except getopt.GetoptError:
        printUsage()
        sys.exit(2)
    for opt, arg in opts:
        if opt == '-h':
            printUsage()
            sys.exit()
        elif opt in ("-f"):
            filePath = arg
        elif opt in ("-i"):
            includeDirectoriesFilePath = arg
        elif opt in ("-o"):
            iwyuOptionsFilePath = arg

    AnalyzeAndFixFile(filePath, includeDirectoriesFilePath, iwyuOptionsFilePath)
    
if __name__ == "__main__":
   import sys
   main(sys.argv[1:])