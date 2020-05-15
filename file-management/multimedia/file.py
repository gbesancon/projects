import os
import datetime
import stat

class File:
    def __init__(self, file_path: str):
        self.file_path: str = file_path

    def get_file_path(self) -> str:
        return self.file_path

    def get_file_name(self) -> str:
        return os.path.basename(self.get_file_path())

    def get_folder(self) -> any:
        from folder import Folder
        return Folder(os.path.dirname(self.get_file_path()))

    def get_file_extension(self) -> str:
        extensions = os.path.splitext(self.get_file_name())
        return extensions[len(extensions) - 1].lower()

    def exists(self) -> bool:
        return os.path.exists(self.file_path)

    def _get_creation_date(self) -> datetime.datetime:
        creation_date = None
        """
        Try to get the date that a file was created, falling back to when it was
        last modified if that isn't possible.
        See http://stackoverflow.com/a/39501288/1709587 for explanation.
        """
        import platform
        if platform.system() == 'Windows':
            creation_date = os.path.getctime(self.file_path)
        else:
            stat = os.stat(self.file_path)
            try:
                creation_date = stat.st_birthtime
            except AttributeError:
                # We're probably on Linux. No easy way to get creation dates here,
                # so we'll settle for when its content was last modified.
                creation_date = stat.st_mtime
        return datetime.datetime.fromtimestamp(creation_date)

    def _set_creation_date(self, file_date: datetime.datetime):
        try:
            import platform
            if platform.system() == "Windows":
                import pywintypes, win32file, win32con
                wintime = pywintypes.Time(file_date)
                winfile = win32file.CreateFile(
                    self.file_path, win32con.GENERIC_WRITE,
                    win32con.FILE_SHARE_READ | win32con.FILE_SHARE_WRITE | win32con.FILE_SHARE_DELETE,
                    None, win32con.OPEN_EXISTING,
                    win32con.FILE_ATTRIBUTE_NORMAL, None)
                win32file.SetFileTime(winfile, wintime, None, None)
                winfile.close()
            elif platform.system() == "Linux":
                pass
        except:
            pass

    def _get_access_date(self) -> datetime.datetime:
        access_date = os.path.getatime(self.file_path) 
        return datetime.datetime.fromtimestamp(access_date)

    def _set_access_date(self, file_date: datetime.datetime):
        try:
            import time
            st = os.stat(self.file_path)
            atime = time.mktime(file_date.timetuple())
            mtime = st[stat.ST_MTIME]
            os.utime(self.file_path, (atime, mtime))
        except:
            pass

    def _get_modification_date(self) -> datetime.datetime:
        modification_date = os.path.getmtime(self.file_path) 
        return datetime.datetime.fromtimestamp(modification_date)

    def _set_modification_date(self, file_date: datetime.datetime):
        try:
            import time
            st = os.stat(self.file_path)
            atime = st[stat.ST_ATIME]
            mtime = time.mktime(file_date.timetuple())
            os.utime(self.file_path, (atime, mtime))
        except:
            pass
    
    def get_file_date(self, use_folder_date: bool) -> datetime.datetime:
        return None

    def _set_file_date_in_file_creation_modification(self, file_date: datetime.datetime):
        self._set_creation_date(file_date)
        self._set_modification_date(file_date)
        self._set_access_date(file_date)

    def set_file_date(self, file_date: datetime.datetime):
        self._set_file_date_in_file_creation_modification(file_date)

    def move_file_to_folder(self, target_folder_path: str):
        file_name = self.get_file_name()
        target_file_path = os.path.join(target_folder_path, file_name)
        self.rename_file(target_file_path)

    def rename_file(self, target_file_path: str):
        try:
            target_file = File(target_file_path)
            if self.exists() and not os.path.exists(target_file_path):
                target_folder = target_file.get_folder()
                if not target_folder.exists():
                    os.makedirs(target_folder.get_folder_path())
                try:
                    os.rename(self.file_path, target_file.get_file_path())
                except:
                    try:
                        import shutil
                        shutil.move(self.file_path, target_file.get_file_path())
                    except:
                        pass
                if target_file.exists():
                    if self.exists():
                        try:
                            os.remove(self.file_path)
                        except:
                            try:
                                os.remove(target_file.get_file_path())
                            except:
                                pass
                    if not self.exists():
                        self.file_path = target_file.get_file_path()
        except:
            pass

def check_file_dates(file_path, label_date1, date1, label_date2, date2, rel_tol=1e-09, abs_tol=0.0):
    valid = False
    date = None
    error_message = None
    if date1:
        if date2:
            def isclose(a, b, rel_tol=1e-09, abs_tol=0.0):
                return abs(a-b) <= max(rel_tol * max(abs(a), abs(b)), abs_tol)
            if isclose((date1 - date1.min).total_seconds(), (date2 - date2.min).total_seconds(), rel_tol, abs_tol):
                valid = True
                date = date1
            else:
                date = date1
                error_message = label_date1 + " (" + str(date1) + ")" + " and " +  label_date2 + " (" + str(date2) + ")" + " not matching."
        else:
            date = date1
            error_message = label_date2 + " is undefined."
    else:
        error_message = label_date1 + " is undefined."
    return (valid, date, error_message)
