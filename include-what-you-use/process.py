def RunCommand(command):
    import subprocess    
    #print(command)
    def run_command(command):
        p = subprocess.Popen(command, stdout=subprocess.PIPE, stderr=subprocess.STDOUT)
        return iter(p.stdout.readline, b'')
    
    start=False
    result=""
    for l in run_command(command):
        line=l.decode("utf8")
        if " should add these lines:" in line:
            start=True
        if start:
            result+=line

    return result