from typing import List, Tuple
import file
import re
import os
import datetime
import file_messages
import multimedia_file
import folder

class PictureFile(multimedia_file.MultimediaFile):
    OLD_PREFIX: str = "p"
    REGULAR_PREFIX: str = "IMG"
    PANORAMA_PREFIX: str = "PAN"
    EXTENSIONS: List[str] = [    
        ".bmp",
        ".gif",
        ".orf",
        ".jpeg",
        ".jpg",
        ".png",
        ".tif",
        ".tiff"
    ]

    def __init__(self, file_path: str):
        super().__init__(file_path)

    def get_prefix(self) -> str:
        return (PictureFile.PANORAMA_PREFIX 
            if self.get_folder().is_panorama_folder() 
                or self.get_file_name().startswith(PictureFile.PANORAMA_PREFIX)
                or self._is_valid_panorama_old_file_name() 
            else PictureFile.REGULAR_PREFIX)

    def _check_regular_file_name(self, file_date: datetime.datetime):
        return self._check_file_name(PictureFile.REGULAR_PREFIX, file_date)

    def _is_valid_regular_file_name(self, file_date: datetime.datetime) -> bool:
        (valid, _) = self._check_regular_file_name(file_date)
        return valid

    def _check_panorama_file_name(self, file_date: datetime.datetime):
        return self._check_file_name(PictureFile.PANORAMA_PREFIX, file_date)

    def _is_valid_panorama_file_name(self, file_date: datetime.datetime) -> bool:
        (valid, _) = self._check_panorama_file_name(file_date)
        return valid
        
    def _is_valid_panorama_old_file_name(self) -> bool:
        valid = False
        file_name = self.get_file_name()
        file_name_no_extension = os.path.splitext(file_name)[0]
        match = re.match(r"^" + PictureFile.OLD_PREFIX + r"(\d\d\d\d\d)" + r" - " + PictureFile.OLD_PREFIX + r"(\d\d\d\d\d\d)" + r"$", file_name_no_extension)
        if match:
            valid = True
        else:
            valid = False
        return valid
        
    def check_file_name(self, file_date: datetime.datetime):
        valid = False
        error_message = None

        if not self.get_folder().is_panorama_folder():
            (valid, error_message) = self._check_regular_file_name(file_date)
            if not valid:
                (valid, error_message) = self._check_panorama_file_name(file_date)
                if not valid:
                    valid = False
                    error_message = "Filename incorrect"
                else:
                    valid = False
                    error_message = "Panorama file not stored in a Panorama folder"
        else:
            (valid, error_message) = self._check_panorama_file_name(file_date)
            if not valid:
                (valid, error_message) = self._check_regular_file_name(file_date)
                if valid:
                    valid = False
                    error_message = "File should not be stored in a Panorama folder"
                else:
                    valid = False
                    error_message = "Filename incorrect"
        return (valid, error_message)

    def _process_move_file(self, use_folder_date, process, verbose) -> Tuple[bool, str]:
        file_process_comment = None
        file_date = self.get_file_date(use_folder_date)
        _folder = self.get_folder()
        if _folder.is_panorama_folder():
            if not (self._is_valid_panorama_file_name(file_date) or self._is_valid_panorama_old_file_name()):
                parent_folder = _folder.get_parent_folder()
                if process:
                    self.move_file_to_folder(parent_folder.get_folder_path())
                if not process or verbose:
                    file_process_comment = "Move to " + parent_folder.get_folder_path()
        else:
            if self._is_valid_panorama_file_name(file_date) or self._is_valid_panorama_old_file_name():
                panorama_picture_folder_path = os.path.join(self.get_folder().get_folder_path(), folder.Folder.PANORAMA_FOLDER_NAME)
                if process:
                    self.move_file_to_folder(panorama_picture_folder_path)
                if not process or verbose:
                    file_process_comment = "Move to " + panorama_picture_folder_path
        return (process, file_process_comment)
