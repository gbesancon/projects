def AnalyzeDirectory(directory, includeDirectoriesFilePath, iwyuOptionsFilePath, logDir):
    import os
    from os.path import join, relpath
    import analyze_file
    directoryName = os.path.basename(os.path.normpath(directory))
    directoryLogDir = join(logDir, directoryName)
    if not os.path.exists(directoryLogDir):
        os.makedirs(directoryLogDir)
    file=open(join(logDir, directoryName + ".txt"), "w")
    extensions=(".h", ".hpp", ".c", ".cpp", ".inl")
    for root, dirs, files in os.walk(directory):
        for name in files:
            if name.endswith(extensions):
                file.write("------------------------------------" + '-' * (len(name) + 2) + "------------------------------------\n")
                file.write("------------------------------------ " + name + " ------------------------------------\n")
                file.write("------------------------------------" + '-' * (len(name) + 2) + "------------------------------------\n")
                logFileName=name + ".txt"
                fileResult=analyze_file.AnalyzeFile(join(root, name), includeDirectoriesFilePath, iwyuOptionsFilePath, True, join(directoryLogDir, logFileName))
                file.write(fileResult)
                file.write("\n\n\n\n")
                file.flush()
                os.fsync(file)
    file.close()
    
def main(argv):
    import sys, getopt
    directory = ''
    includeDirectoriesFilePath = ''
    iwyuOptionsFilePath = ''
    logDirectoryPath = ''

    def printUsage():
        print('analyze_folder.py -d <directory> -i <includeDirectoriesFilePath> -o <iwyuOptionsFilePath> -l <logDirectoryPath>')

    try:
        opts, args = getopt.getopt(argv,"hd:i:o:l:")
    except getopt.GetoptError:
        printUsage()
        sys.exit(2)
    for opt, arg in opts:
        if opt == '-h':
            printUsage()
            sys.exit()
        elif opt in ("-d"):
            directory = arg
        elif opt in ("-i"):
            includeDirectoriesFilePath = arg
        elif opt in ("-o"):
            iwyuOptionsFilePath = arg
        elif opt in ("-l"):
            logDirectoryPath = arg

    AnalyzeDirectory(directory, includeDirectoriesFilePath, iwyuOptionsFilePath, logDirectoryPath)
    
if __name__ == "__main__":
   import sys
   main(sys.argv[1:])