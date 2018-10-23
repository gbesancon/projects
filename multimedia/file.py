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


def isclose(a, b, rel_tol=1e-09, abs_tol=0.0):
    return abs(a-b) <= max(rel_tol * max(abs(a), abs(b)), abs_tol)

def check_file_dates(file_path, label_date1, get_date1, label_date2, get_date2, rel_tol=1e-09, abs_tol=0.0):
    valid = False
    date = None
    error_message = None
    date1 = get_date1(file_path)
    date2 = get_date2(file_path)
    if date1:
        if date2:
            if date1.year == date2.year and date1.month == date2.month and date1.day == date2.day:
                if isclose(date1.timestamp(), date2.timestamp(), rel_tol, abs_tol):
                    valid = True
                    date = date1
                else:
                    date = date1
                    error_message = label_date1 + " (" + str(date1) + ")" + " and " +  label_date2 + " (" + str(date2) + ")" + " not matching."
            else:
                date = date1
                error_message = label_date1 + " (" + str(date1) + ")" + " and " +  label_date2 + " (" + str(date2) + ")" + " not matching."
        else:
            date = date1
            error_message = label_date2 + " is undefined."
    else:
        error_message = label_date1 + " is undefined."
    return (valid, date, error_message)
