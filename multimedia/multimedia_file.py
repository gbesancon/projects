from typing import Tuple, List
import re
import datetime
import os
import file
import piexif

class MultimediaFile(file.File):
    def __init__(self, file_path):
        super().__init__(file_path)

    def get_prefix(self) -> str:
        raise NotImplementedError
    
    def _get_hachoir_metadata(self):
        metadata = None
        try:
            from hachoir.parser import createParser
            parser = createParser(self.file_path)
            if parser:
                with parser:
                    from hachoir.metadata import extractMetadata
                    metadata = extractMetadata(parser)
        except:
            pass
        return metadata

    def _has_hachoir_metadata(self):
        return self._get_hachoir_metadata() is not None

    def _get_hachoir_metadata_values(self, key: str):
        values = []
        metadata = self._get_hachoir_metadata()
        if metadata:
            if key in metadata._Metadata__data:
                metadata_values = metadata._Metadata__data[key].values
                if len(metadata_values) > 0:
                    for metadata_value in metadata_values:
                        values.append(metadata_value.value)
        return values

    def _has_hachoir_metadata_key(self, key: str):
        return len(self._get_hachoir_metadata_values(key)) > 0

    def _get_hachoir_metadata_value(self, key: str, index: int=0):
        value = None
        values = self._get_hachoir_metadata_values(key)
        if len(values) > index:
            value = values[index]
        return value

    def _has_exif(self) -> bool:
        return self._get_exif() is not None

    def _get_exif(self):
        exif_dict = None
        try:
            exif_dict = piexif.load(self.file_path)
        except:
            pass
        return exif_dict
    
    def _create_exif(self):
        exif_dict = None
        try:
            zeroth_ifd = {}
            exif_ifd = {}
            gps_ifd = {}
            first_ifd = {}
            exif_dict = {"0th":zeroth_ifd, "Exif":exif_ifd, "GPS":gps_ifd, "1st":first_ifd}
            exif_bytes = piexif.dump(exif_dict)
            piexif.insert(exif_bytes, self.file_path)
        except:
            pass
        exif_dict = self._get_exif()
        return exif_dict

    UTF8: str = "utf-8"
    EXIF_DATE_FORMAT: str = r'%Y:%m:%d %H:%M:%S'

    def _has_exif_date_time_original(self) -> bool:
        return self._get_exif_date_time_original() is not None

    def _get_exif_date_time_original(self) -> datetime.datetime:
        exif_date_time_original = None
        try:
            if self._has_exif():
                exif_dict = self._get_exif()
                if "Exif" in exif_dict:
                    exif = exif_dict["Exif"]
                    if exif:
                        if piexif.ExifIFD.DateTimeOriginal in exif:
                            date_time_original_binary = exif[piexif.ExifIFD.DateTimeOriginal]
                            if date_time_original_binary:
                                date_time_original = date_time_original_binary.decode(MultimediaFile.UTF8)
                                exif_date_time_original = datetime.datetime.strptime(date_time_original, MultimediaFile.EXIF_DATE_FORMAT)
        except:
            pass
        return exif_date_time_original

    def _set_exif_date_time_original(self, file_date: datetime.datetime):
        try:
            if not self._has_exif():
                self._create_exif()
            if self._has_exif():
                exif_dict = self._get_exif()
                exif_dict["Exif"][piexif.ExifIFD.DateTimeOriginal] = file_date.strftime(MultimediaFile.EXIF_DATE_FORMAT).encode(MultimediaFile.UTF8)
                exif_bytes = piexif.dump(exif_dict)
                piexif.insert(exif_bytes, self.file_path)
        except:
            pass

    def _has_exif_date_time_digitized(self) -> bool:
        return self._get_exif_date_time_digitized() is not None

    def _get_exif_date_time_digitized(self) -> datetime.datetime:
        exif_date_time_digitized = None
        try:
            if self._has_exif():
                exif_dict = self._get_exif()
                if "Exif" in exif_dict:
                    exif = exif_dict["Exif"]
                    if exif:
                        if piexif.ExifIFD.DateTimeDigitized in exif:
                            date_time_digitized_binary = exif[piexif.ExifIFD.DateTimeDigitized]
                            if date_time_digitized_binary:
                                date_time_digitized = date_time_digitized_binary.decode(MultimediaFile.UTF8)
                                exif_date_time_digitized = datetime.datetime.strptime(date_time_digitized, MultimediaFile.EXIF_DATE_FORMAT)
        except:
            pass
        return exif_date_time_digitized

    def _set_exif_date_time_digitized(self, file_date: datetime.datetime):
        try:
            if not self._has_exif():
                self._create_exif()
            if self._has_exif():
                exif_dict = self._get_exif()
                exif_dict["Exif"][piexif.ExifIFD.DateTimeDigitized] = file_date.strftime(MultimediaFile.EXIF_DATE_FORMAT).encode(MultimediaFile.UTF8)
                exif_bytes = piexif.dump(exif_dict)
                piexif.insert(exif_bytes, self.file_path)
        except:
            pass
            
    def _is_valid_file_name(self, file_name_prefix: str):
        valid = False
        date = None
        try:
            file_name = self.get_file_name()
            file_name_no_extension = os.path.splitext(file_name)[0]
            match = re.match(r"^" + file_name_prefix + r"_" + r"(\d\d\d\d\d\d\d\d_\d\d\d\d\d\d)" + r".*$", file_name_no_extension)
            if match:
                valid = True
                date = datetime.datetime.strptime(match.group(1), r'%Y%m%d_%H%M%S')
            else:
                valid = False
        except:
            valid = False
        return (valid, date)
    
    def _check_file_name(self, file_name_prefix: str, file_date: datetime.datetime):
        valid = False
        error_message = None
        (file_name_valid, file_name_date) = self._is_valid_file_name(file_name_prefix)
        if file_name_valid:
            (valid, _, error_message) = file.check_file_dates(self.file_path, "File name date", file_name_date, "File date", file_date)
        else:
            valid = file_name_valid
            error_message = "Filename incorrect"
        return (valid, error_message)

    def check_file_name(self, file_date: datetime.datetime):
        return self._check_file_name(self.get_prefix(), file_date)

    def generate_file_name(self, use_folder_date: bool):
        file_name = None
        (file_date_valid, file_date) = self.get_file_date(use_folder_date)
        if file_date_valid:
            file_extension = self.get_file_extension()
            file_name = file_date.strftime(self.get_prefix() + "_" + "%Y%m%d_%H%M%S" + file_extension)
        else:
            file_extension = self.get_file_extension()
            file_name = self.get_prefix() + "_" + "00000000_000000" + file_extension
        return file_name

    CREATION_DATE: str = "creation_date"
    DATE_TIME_ORIGINAL: str = "date_time_original"
    DATE_TIME_DIGITIZED: str = "date_time_digitized"

    def _get_file_date_from_file_name(self) -> Tuple[bool, datetime.datetime]:
        file_date_valid = False
        file_date = None
        (file_date_valid, file_date) = self._is_valid_file_name(self.get_prefix())
        return (file_date_valid, file_date)

    def _get_file_date_from_metadata(self) -> Tuple[bool, datetime.datetime]:
        file_date_valid = False
        file_date = None
        if not file_date_valid:
            if self._has_exif():
                if not file_date_valid and self._has_exif_date_time_original():
                    file_date_valid = True
                    file_date = self._get_exif_date_time_original()
                elif not file_date_valid and self._has_exif_date_time_digitized():
                    file_date_valid = True
                    file_date = self._get_exif_date_time_digitized()
        if not file_date_valid:
            if self._has_hachoir_metadata():
                #if not file_date_valid and self._has_hachoir_metadata_key(MultimediaFile.DATE_TIME_ORIGINAL):
                #    file_date_valid = True
                #    file_date = self._get_hachoir_metadata_value(MultimediaFile.DATE_TIME_ORIGINAL)
                #if not file_date_valid and self._has_hachoir_metadata_key(MultimediaFile.DATE_TIME_DIGITIZED):
                #    file_date_valid = True
                #    file_date = self._get_hachoir_metadata_value(MultimediaFile.DATE_TIME_DIGITIZED)
                if not file_date_valid and self._has_hachoir_metadata_key(MultimediaFile.CREATION_DATE):
                    file_date_valid = True
                    file_date = self._get_hachoir_metadata_value(MultimediaFile.CREATION_DATE)
        return (file_date_valid, file_date)

    def _get_file_date_from_location(self) -> Tuple[bool, datetime.datetime]:
        folder_date_valid = False
        folder_date = None
        (folder_date_valid, folder_date) = self.get_folder().has_valid_single_day_dated_folder_name()
        return (folder_date_valid, folder_date)

    def get_file_date(self, use_folder_date: bool) -> Tuple[bool, datetime.datetime]:
        file_date_valid = False
        file_date = None
        (file_date_valid, file_date) = self._get_file_date_from_file_name()
        if not file_date_valid:
            (file_date_valid, file_date) = self._get_file_date_from_metadata()
            if not file_date_valid and use_folder_date:
                (file_date_valid, file_date) = self._get_file_date_from_location()
        return (file_date_valid, file_date)

    def _set_file_date_in_metadata(self, file_date: datetime.datetime):
        self._set_exif_date_time_original(file_date)
        self._set_exif_date_time_digitized(file_date)

    def set_file_date(self, file_date: datetime.datetime):
        super().set_file_date(file_date)
        self._set_file_date_in_metadata(file_date)

    def _check_file_date_with_metadata(self, file_date: datetime.datetime) -> Tuple[bool, str]:
        file_date_valid = True
        file_error_message = None
        if file_date_valid and self._has_exif():
            if file_date_valid and self._has_exif_date_time_original():
                (file_date_valid, _, file_error_message) = file.check_file_dates(self.file_path, "File date", file_date, "EXIF Date Time Original", self._get_exif_date_time_original())
            if file_date_valid and self._has_exif_date_time_digitized():
                (file_date_valid, _, file_error_message) = file.check_file_dates(self.file_path, "File date", file_date, "EXIF Date Time Digitized", self._get_exif_date_time_digitized())
        return (file_date_valid, file_error_message)

    def _check_file_date_with_file_creation_modification(self, file_date: datetime.datetime) -> Tuple[bool, str]:
        file_date_valid = True
        file_error_message = None
        if file_date_valid:
            (file_date_valid, _, file_error_message) = file.check_file_dates(self.file_path, "File date", file_date, "Creation date", self._get_creation_date())
        if False and file_date_valid:
            (file_date_valid, _, file_error_message) = file.check_file_dates(self.file_path, "File date", file_date, "Modification date", self._get_modification_date())
        if file_date_valid:
            (file_date_valid, _, file_error_message) = file.check_file_dates(self.file_path, "File date", file_date, "Access date", self._get_access_date())
        return (file_date_valid, file_error_message)

    def check_file_date(self, file_date: datetime.datetime) -> Tuple[bool, str]:
        (file_date_valid, file_error_message) = self._check_file_date_with_file_creation_modification(file_date)
        if file_date_valid:
            (file_date_valid, file_error_message) = self._check_file_date_with_metadata(file_date)
        return (file_date_valid, file_error_message)
            
    def check_file_location(self, file_date: datetime.datetime) -> Tuple[bool, str]:
        file_location_valid = True
        file_location_error_message = None    
        (folder_date_valid, folder_date) = self.get_folder().has_valid_single_day_dated_folder_name()
        if folder_date_valid:
            (file_location_valid, _, file_location_error_message) = file.check_file_dates(self.file_path, "File date", file_date, "Folder date", folder_date, 24*60*60)
            if not file_location_valid:
                file_location_error_message = "File date (" + str(file_date) + ") shouldn't be stored in folder " + self.get_folder().get_folder_path()
        else:
            (folder_date_valid, folder_beginning_date, folder_end_date) = self.get_folder().has_valid_period_dated_folder_name()
            if folder_date_valid:
                (file_location_valid, _, file_location_error_message) = file.check_file_dates(self.file_path, "File date", file_date, "Folder date", folder_beginning_date, folder_end_date.timestamp() - folder_beginning_date.timestamp())
                if not file_location_valid:
                    file_location_error_message = "File date (" + str(file_date) + ") shouldn't be stored in folder " + self.get_folder().get_folder_path()
        return (file_location_valid, file_location_error_message)

    def check_file(self, use_folder_date: bool, verbose: bool) -> Tuple[bool, str]:
        (file_valid, file_date) = self.get_file_date(use_folder_date)
        if file_valid:
            (file_valid, file_error_message) = self.check_file_name(file_date)
            if file_valid:
                (file_valid, file_error_message) = self.check_file_date(file_date)
                if file_valid:
                    (file_valid, file_error_message) = self.check_file_location(file_date)
        else:
            file_error_message = "File date undefined"
        return (file_valid, file_error_message)

    def _process_set_file_date(self, use_folder_date: bool, process: bool, verbose: bool) -> Tuple[bool, str]:
        file_process_comment = None
        (file_date_valid, file_date) = self.get_file_date(use_folder_date)
        if file_date_valid:
            if process:
                self.set_file_date(file_date)
            if not process or verbose:
                file_process_comment = "Set File date (" + str(file_date) + ")"
        return (process, file_process_comment)

    def _process_move_file(self, use_folder_date: bool, process: bool, verbose: bool) -> Tuple[bool, str]:
        file_process_comment = None
        return (process, file_process_comment)

    def _process_rename_file(self, use_folder_date: bool, process: bool, verbose: bool) -> Tuple[bool, str]:
        file_process_comment = None
        folder_path = self.get_folder().get_folder_path()
        file_name = self.get_file_name()
        file_name_without_extension = os.path.splitext(file_name)[0]
        new_file_name = self.generate_file_name(use_folder_date)
        new_file_name_without_extension = os.path.splitext(new_file_name)[0]
        if not new_file_name_without_extension == file_name_without_extension:
            if not self.exists():
                self.file_path += ".tmp"
            if self.exists():
                def get_new_available_file_name(folder_path, new_file_name):
                    new_file_path = os.path.join(folder_path, new_file_name)
                    new_file_name_without_extension = os.path.splitext(new_file_name)[0]
                    new_file = file.File(new_file_path)
                    new_file_extension = new_file.get_file_extension()
                    available_file_name = new_file_name
                    index = 1
                    while(os.path.exists(os.path.join(folder_path, available_file_name))):
                        available_file_name = new_file_name_without_extension + "_" + str(index) + new_file_extension
                        index += 1
                    return available_file_name
                new_file_name = get_new_available_file_name(folder_path, new_file_name)
                new_file_path = os.path.join(folder_path, new_file_name)
                if not os.path.exists(new_file_path):
                    if process:
                        self.rename_file(new_file_path)
                    file_process_comment = "Renamed " + new_file_name
        return (process, file_process_comment)

    def process_file(self, use_folder_date: bool, set_dates: bool, move_files: bool, rename_files: bool, process: bool, verbose: bool) -> Tuple[bool, List[str]]:
        file_processed = True
        file_process_comments = []  
        if set_dates:
            # Change file dates
            (file_date_processed, file_date_process_comment) = self._process_set_file_date(use_folder_date, process, verbose)
            file_processed &= file_date_processed
            if (not file_date_processed or verbose) and file_date_process_comment:
                file_process_comments.append(file_date_process_comment)
        if move_files:
            # Move files
            (move_file_processed, move_file_process_comment) = self._process_move_file(use_folder_date, process, verbose)
            file_processed &= move_file_processed
            if (not move_file_processed or verbose) and move_file_process_comment:
                file_process_comments.append(file_date_process_comment)
        if rename_files:
            # Rename files
            (rename_file_processed, rename_file_process_comment) = self._process_rename_file(use_folder_date, process, verbose)
            file_processed &= rename_file_processed
            if (not rename_file_processed or verbose) and rename_file_process_comment:
                file_process_comments.append(file_date_process_comment)
        return (file_processed, file_process_comments)
