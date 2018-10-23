import os
import datetime
import platform
import file
import time

def get_folder_path(file_path):
    return os.path.dirname(file_path)

def get_folder_name(file_path):    
    folder_path = get_folder_path(file_path)
    return os.path.basename(folder_path)

def get_file_name(file_path):
    return os.path.basename(file_path)

def get_file_extension(file_path):
    extensions = os.path.splitext(get_file_name(file_path))
    return extensions[len(extensions) - 1].lower()

def move_file_to_folder(file_path, target_folder_path):
    try:
        file_name = file.get_file_name(file_path)
        target_file_path = os.path.join(target_folder_path, file_name)
        if os.path.exists(file_path) and not os.path.exists(target_file_path):
            if not os.path.exists(target_folder_path):
                os.makedirs(target_folder_path)
            import shutil
            shutil.move(file_path, target_file_path)
    except:
        pass

def get_creation_date(file_path):
    creation_date = None
    """
    Try to get the date that a file was created, falling back to when it was
    last modified if that isn't possible.
    See http://stackoverflow.com/a/39501288/1709587 for explanation.
    """
    if platform.system() == 'Windows':
        creation_date = os.path.getctime(file_path)
    else:
        stat = os.stat(file_path)
        try:
            creation_date = stat.st_birthtime
        except AttributeError:
            # We're probably on Linux. No easy way to get creation dates here,
            # so we'll settle for when its content was last modified.
            creation_date = stat.st_mtime
    return datetime.datetime.fromtimestamp(creation_date)

def set_creation_date(file_path, file_date):
    try:
        if platform.system() == "Windows":
            import pywintypes, win32file, win32con
            wintime = pywintypes.Time(file_date)
            winfile = win32file.CreateFile(
                file_path, win32con.GENERIC_WRITE,
                win32con.FILE_SHARE_READ | win32con.FILE_SHARE_WRITE | win32con.FILE_SHARE_DELETE,
                None, win32con.OPEN_EXISTING,
                win32con.FILE_ATTRIBUTE_NORMAL, None)
            win32file.SetFileTime(winfile, wintime, None, None)
            winfile.close()
        elif platform.system() == "Linux":
            pass
    except:
        pass

def get_modification_date(file_path):
    modification_date = os.path.getmtime(file_path) 
    return datetime.datetime.fromtimestamp(modification_date)

def set_modification_date(file_path, file_date):
    try:
        file_time = time.mktime(file_date.timetuple())
        os.utime(file_path, (file_time, file_time))
    except:
        pass
        
def set_file_date(file_path, file_date):
    set_creation_date(file_path, file_date)
    set_modification_date(file_path, file_date)