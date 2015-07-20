def AnalyzeFile(filePath, includeDirectoriesFilePath, iwyuOptionsFilePath, createLog, logFilePath):
    import os
    from os.path import join, relpath
    import process
    command='include-what-you-use.exe'
    with open(includeDirectoriesFilePath) as includeDirectoriesFile:
        includeDirectories = includeDirectoriesFile.readlines()
        for includeDirectory in includeDirectories:
            command+= ' -I ' + includeDirectory
    with open(iwyuOptionsFilePath) as iwyuOptionsFile:
        iwyuOptions = iwyuOptionsFile.readlines()
        for iwyuOption in iwyuOptions:
            command+= ' ' + iwyuOption
    command+= ' "' + filePath + '"'
    
    print("Analyzing " + filePath)
    result = process.RunCommand(command)
    #print(result)

    if createLog:
        file=open(logFilePath, "w")
        file.write(result)
        file.flush()
        os.fsync(file)
        file.close()

    return result
