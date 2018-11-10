import file
import re
import os
import piexif
import datetime
import file_messages
import multimedia_file
import exif_picture_file
import metadata_utility

OLD_PREFIX = "p"
REGULAR_PREFIX = "IMG"
PANORAMA_PREFIX = "PAN"
NON_EXIF_EXTENSIONS = [    
    ".bmp",
    ".gif",
    ".orf",
]
EXTENSIONS = NON_EXIF_EXTENSIONS + exif_picture_file.EXIF_EXTENSIONS

PANORAMA_FOLDER_NAME = "Panorama"

def is_panorama_folder_name(folder_name):
    return folder_name == PANORAMA_FOLDER_NAME

def is_panorama_folder_path(folder_path):
    folder_name = os.path.basename(folder_path)
    return is_panorama_folder_name(folder_name)

def is_file(file_path):
    file_extension = file.get_file_extension(file_path)
    return file_extension in EXTENSIONS

def generate_file_name(file_path, use_folder_date):
    file_name = None
    folder_path = file.get_folder_path(file_path)
    prefix = PANORAMA_PREFIX if is_panorama_folder_path(folder_path) else REGULAR_PREFIX
    (file_date_valid, file_date) = get_file_date(file_path, use_folder_date)
    if file_date_valid:
        file_extension = file.get_file_extension(file_path)
        file_name = file_date.strftime(prefix + "_" + "%Y%m%d_%H%M%S" + file_extension)
    if not file_name:
        file_extension = file.get_file_extension(file_path)
        file_name = prefix + file_extension
    return file_name

def check_regular_file_name(file_path):
    return multimedia_file.check_multimedia_file_name(file_path, REGULAR_PREFIX)

def is_valid_regular_file_name(file_path):
    (valid, _, _) = check_regular_file_name(file_path)
    return valid

def check_panorama_file_name(file_path):
    return multimedia_file.check_multimedia_file_name(file_path, PANORAMA_PREFIX)

def is_valid_panorama_file_name(file_path):
    (valid, _, _) = check_panorama_file_name(file_path)
    return valid
    
def check_file_name(file_path):
    valid = False
    file_date = None
    error_message = None
    folder_name = file.get_folder_name(file_path)
    if not is_panorama_folder_name(folder_name):
        (valid, file_date, error_message) = check_regular_file_name(file_path)
        if not valid:
            (valid, file_date, error_message) = check_panorama_file_name(file_path)
            if valid:
                valid = False
                file_date = None
                error_message = "Panorama file not stored in a Panorama folder"
            else:
                valid = False
                file_date = None
                error_message = "Filename incorrect"
    else:
        (valid, file_date, error_message) = check_panorama_file_name(file_path)
        if not valid:
            (valid, file_date, error_message) = check_regular_file_name(file_path)
            if valid:
                valid = False
                file_date = None
                error_message = "File should not be stored in a Panorama folder"
            else:
                valid = False
                file_date = None
                error_message = "Filename incorrect"
    return (valid, file_date, error_message)

def get_file_date(file_path, use_folder_date):
    (file_date_valid, file_date, _) = check_file_name(file_path)
    if not file_date_valid and exif_picture_file.has_exif_date_time_original(file_path):
        file_date_valid = True
        file_date = exif_picture_file.get_exif_date_time_original(file_path)
    elif not file_date_valid and exif_picture_file.has_exif_date_time_digitized(file_path):
        file_date_valid = True
        file_date = exif_picture_file.get_exif_date_time_digitized(file_path)
    else:
        (file_date_valid, file_date, _) = check_file_name(file_path)
        if not file_date_valid and use_folder_date:
            (folder_date_valid, folder_date) = multimedia_file.has_valid_dated_folder_name(file_path)
            if folder_date_valid:
                file_date_valid = True
                file_date = folder_date
    return (file_date_valid, file_date)

def set_file_date(file_path, file_date):
    exif_picture_file.set_exif_date_time_original(file_path, file_date)
    exif_picture_file.set_exif_date_time_digitized(file_path, file_date)
    file.set_file_date(file_path, file_date)

def check_file_date(file_path, use_folder_date):
    (file_date_valid, file_date) = get_file_date(file_path, use_folder_date)
    file_error_message = None
    if file_date_valid:
        if exif_picture_file.has_exif_date_time_original(file_path):
            if file_date_valid:
                (file_date_valid, file_date, file_error_message) = file.check_file_dates(file_path, "File date", lambda f : file_date, "EXIF Date Time Original", exif_picture_file.get_exif_date_time_original)
        if exif_picture_file.has_exif_date_time_digitized(file_path):
            if file_date_valid:
                (file_date_valid, file_date, file_error_message) = file.check_file_dates(file_path, "File date", lambda f : file_date, "EXIF Date Time Digitized", exif_picture_file.get_exif_date_time_digitized)
        if file_date_valid:
            (file_date_valid, file_date, file_error_message) = multimedia_file.check_multimedia_file_date(file_path, file_date)
    else:
        file_error_message = "No date identified for file"
    return (file_date_valid, file_date, file_error_message)
    
def process_set_file_date(file_path, use_folder_date, process, verbose):
    file_process_comment = None
    (file_date_valid, file_date) = get_file_date(file_path, use_folder_date)
    (folder_date_valid, folder_date) = multimedia_file.has_valid_dated_folder_name(file_path)
    if file_date_valid:
        if folder_date_valid:
            if process:
                set_file_date(file_path, file_date)
            if not process or verbose:
                file_process_comment = "Set File date (" + str(file_date) + ")"
        else:
            if folder_date_valid and use_folder_date:
                if process:
                    set_file_date(file_path, folder_date)
                if not process or verbose:
                    file_process_comment = "Set File date (" + str(folder_date) + ")"
    return (process, file_process_comment)

def process_move_file(file_path, use_folder_date, process, verbose):
    file_process_comment = None
    folder_path = file.get_folder_path(file_path)
    if is_panorama_folder_name(os.path.basename(file_path)):
        if not is_valid_panorama_file_name(file_path):
            parent_folder_path = file.get_folder_path(folder_path)
            if process:
                file.move_file_to_folder(file_path, parent_folder_path)
            if not process or verbose:
                file_process_comment = "Move to " + parent_folder_path
    else:
        if is_valid_panorama_file_name(file_path):
            panorama_picture_folder_path = os.path.join(folder_path, PANORAMA_FOLDER_NAME)
            if process:
                file.move_file_to_folder(file_path, panorama_picture_folder_path)
            if not process or verbose:
                file_process_comment = "Move to " + panorama_picture_folder_path
    return (process, file_process_comment)

def process_rename_file(file_path, use_folder_date, process, verbose):
    file_process_comment = None
    folder_path = file.get_folder_path(file_path)
    file_name = file.get_file_name(file_path)
    file_name_without_extension = os.path.splitext(file_name)[0]
    new_file_name = generate_file_name(file_path, use_folder_date)
    new_file_name_without_extension = os.path.splitext(new_file_name)[0]
    if not new_file_name_without_extension.startswith(file_name_without_extension):
        if not os.path.exists(file_path):
            file_path += ".tmp"
        if os.path.exists(file_path):
            def get_new_available_file_name(folder_path, new_file_name):
                new_file_name_without_extension = os.path.splitext(new_file_name)[0]
                new_file_extension = file.get_file_extension(new_file_name)
                available_file_name = new_file_name
                index = 0
                while(os.path.exists(os.path.join(folder_path, available_file_name))):
                    available_file_name = new_file_name_without_extension + "_" + str(index) + new_file_extension
                    index += 1
                return available_file_name
            new_file_name = get_new_available_file_name(folder_path, new_file_name)
            new_file_path = os.path.join(folder_path, new_file_name)
            if not os.path.exists(new_file_path):
                if process:
                    file.rename_file(file_path, new_file_path)
                file_process_comment = "Renamed " + new_file_name
    return (process, file_process_comment)

def process_file(file_path, use_folder_date, set_dates, move_files, rename_files, process, verbose):
    file_processed = True
    file_process_comments = []
    if set_dates:
        # Change file dates
        (file_date_processed, file_date_process_comment) = process_set_file_date(file_path, use_folder_date, process, verbose)
        file_processed &= file_date_processed
        file_process_comments.append(file_date_process_comment)
    if move_files:
        # Move files
        (move_file_processed, move_file_process_comment) = process_move_file(file_path, use_folder_date, process, verbose)
        file_processed &= move_file_processed
        file_process_comments.append(move_file_process_comment)
    if rename_files:
        # Rename files
        (rename_file_processed, rename_file_process_comment) = process_rename_file(file_path, use_folder_date, process, verbose)
        file_processed &= rename_file_processed
        file_process_comments.append(rename_file_process_comment)    
    return (file_processed, file_process_comments)
