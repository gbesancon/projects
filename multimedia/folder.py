from typing import Tuple, List
import os
import re
import datetime
import file_messages
from file import File
import audio_file
import picture_file
import video_file

class Folder:
    def __init__(self, folder_path: str):
        self.folder_path: str = folder_path

    def get_folder_path(self) -> str:
        return self.folder_path

    def get_folder_name(self) -> str:
        return os.path.basename(self.folder_path)

    def exists(self) -> bool:
        return os.path.exists(self.folder_path)
        
    def get_parent_folder(self):
        return Folder(os.path.dirname(self.folder_path))

    def _is_year_folder_name(self) -> bool:
        valid: bool = True
        folder_name = self.get_folder_name()
        match = re.match(r"^\d\d\d\d$", folder_name)
        if match:
            valid = True
        else:
            valid = False
        return valid

    def _is_valid_single_day_dated_folder_name(self) -> Tuple[bool, datetime.datetime]:
        valid = True
        date = None
        folder_name = self.get_folder_name()
        match = re.match(r"^(\d\d\d\d-\d\d-\d\d)[a-z]?\s-\s.*$", folder_name)
        if match:
            valid = True
            try:
                date = datetime.datetime.strptime(match.group(1), r'%Y-%m-%d')
            except:
                valid = False
                date = None
        else:
            valid = False
        return (valid, date)

    def has_valid_single_day_dated_folder_name(self) -> Tuple[bool, datetime.datetime]:
        valid = False
        folder = self
        while not valid and not folder._is_year_folder_name():
            (valid, date) = folder._is_valid_single_day_dated_folder_name()
            folder = folder.get_parent_folder()
        return (valid, date)

    def _is_valid_period_dated_folder_name(self) -> Tuple[bool, datetime.datetime, datetime.datetime]:
        valid = False
        beginning_date = None
        end_date = None
        folder_name = self.get_folder_name()
        if not valid:
            match = re.match(r"^(\d\d\d\d-\d\d-\d\d)[a-z]?_(\d\d\d\d-\d\d-\d\d)[a-z]?\s-\s.*$", folder_name)
            if match:
                valid = True
                try:
                    beginning_date = datetime.datetime.strptime(match.group(1), r'%Y-%m-%d')
                    end_date = datetime.datetime.strptime(match.group(2), r'%Y-%m-%d') + datetime.timedelta(days=1) - datetime.timedelta(microseconds=1)
                except:
                    valid = False
                    beginning_date = None
                    end_date = None
        if not valid:
            match = re.match(r"^(\d\d\d\d-\d\d-\d\d)[a-z]?_(\d\d-\d\d)[a-z]?\s-\s.*$", folder_name)
            if match:
                valid = True
                try:
                    beginning_date = datetime.datetime.strptime(match.group(1), r'%Y-%m-%d')
                    end_date = datetime.datetime.strptime(str(beginning_date.year) + "-" + match.group(2), r'%Y-%m-%d') + datetime.timedelta(days=1) - datetime.timedelta(microseconds=1)
                except:
                    valid = False
                    beginning_date = None
                    end_date = None
        if not valid:
            match = re.match(r"^(\d\d\d\d-\d\d-\d\d)[a-z]?_(\d\d)[a-z]?\s-\s.*$", folder_name)
            if match:
                valid = True
                try:
                    beginning_date = datetime.datetime.strptime(match.group(1), r'%Y-%m-%d')
                    end_date = datetime.datetime.strptime(str(beginning_date.year) + "-" + str(beginning_date.month) + "-" + match.group(2), r'%Y-%m-%d') + datetime.timedelta(days=1) - datetime.timedelta(microseconds=1)
                except:
                    valid = False
                    beginning_date = None
                    end_date = None
        return (valid, beginning_date, end_date)

    def has_valid_period_dated_folder_name(self) -> Tuple[bool, datetime.datetime, datetime.datetime]:
        valid = False
        folder = self
        while not valid and not folder._is_year_folder_name():
            (valid, beginning_date, end_date) = folder._is_valid_period_dated_folder_name()
            if not valid:
                (valid, date) = folder._is_valid_single_day_dated_folder_name()
                if valid:
                    beginning_date = date
                    end_date = date + datetime.timedelta(days=1) - datetime.timedelta(microseconds=1)
            folder = folder.get_parent_folder()
        return (valid, beginning_date, end_date)
        
    def has_valid_dated_folder_name(self) -> bool:
        (folder_date_valid, _) = self.has_valid_single_day_dated_folder_name()
        if not folder_date_valid:
            (folder_date_valid, _, _) = self.has_valid_period_dated_folder_name()
        return folder_date_valid
        
    PANORAMA_FOLDER_NAME: str = "Panorama"

    def is_panorama_folder(self) -> bool:
        return self.get_folder_name() == Folder.PANORAMA_FOLDER_NAME

    def _get_file(self, file_path) -> File:
        file = File(file_path)        
        file_extension = file.get_file_extension()
        if file_extension in audio_file.AudioFile.EXTENSIONS:
            file = audio_file.AudioFile(file_path)
        elif file_extension in picture_file.PictureFile.EXTENSIONS:
            file = picture_file.PictureFile(file_path)
        elif file_extension in video_file.VideoFile.EXTENSIONS:
            file = video_file.VideoFile(file_path)
        else:
            file = None
        return file

    def check_files(self, use_folder_date, verbose) -> Tuple[bool, List[str]]:
        files_valid = True
        file_names = [f for f in os.listdir(self.folder_path) if os.path.isfile(os.path.join(self.folder_path, f))]
        if len(file_names):
            for file_name in file_names:
                if files_valid:
                    file_path = os.path.join(self.folder_path, file_name)
                    file = self._get_file(file_path)
                    if file:
                        (file_valid, _) = file.check_file(use_folder_date, verbose)
                        files_valid &= file_valid
        return files_valid
