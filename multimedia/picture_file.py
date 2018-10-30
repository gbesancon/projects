import file
import re
import os
import piexif
import datetime
import file_messages
import multimedia_file
import exif_picture_file

PICTURE_PREFIX = "p"
NON_EXIF_PICTURE_EXTENSION = [    
    ".bmp",
    ".gif",
    ".orf",
]
PICTURE_EXTENSION_PREFIX = {}
for file_extension in exif_picture_file.EXIF_PICTURE_EXTENSION + NON_EXIF_PICTURE_EXTENSION:
    PICTURE_EXTENSION_PREFIX[file_extension] = PICTURE_PREFIX

def is_picture_file(file_path):
    file_extension = file.get_file_extension(file_path)
    return file_extension in PICTURE_EXTENSION_PREFIX
    
def check_picture_file_name(file_path):
    valid = False
    error_message = None
    folder_name = file.get_folder_name(file_path)
    (valid, error_message) = multimedia_file.check_multimedia_file_name(file_path, PICTURE_EXTENSION_PREFIX)
    if valid:
        if not is_panorama_folder_name(folder_name):
            valid = True
        else:
            valid = False
            error_message = "File should not be stored in a Panorama folder"
    else:
        if is_valid_panorama_file_name(file_path):
            if is_panorama_folder_name(folder_name):
                valid = True
            else:
                valid = False
                error_message = "Panorama file not stored in a Panorama folder"
        else:
            if is_panorama_folder_name(folder_name):
                valid = False
                error_message = "Panorama filename incorrect"
            else:
                valid = False
                error_message = "Filename incorrect"
    return (valid, error_message)

PANORAMA_FOLDER_NAME = "Panorama"

def is_panorama_folder_name(folder_name):
    return folder_name == PANORAMA_FOLDER_NAME

def is_panorama_folder_path(folder_path):
    folder_name = os.path.basename(folder_path)
    return is_panorama_folder_name(folder_name)
    
def is_valid_panorama_file_name(file_path):
    valid = True
    file_name = file.get_file_name(file_path)
    file_name_no_extension = os.path.splitext(file_name)[0]
    file_extension = file.get_file_extension(file_path)
    if file_extension in PICTURE_EXTENSION_PREFIX:
        prefix = PICTURE_EXTENSION_PREFIX[file_extension]
        match = re.match(r"^" + prefix + r"\d\d\d\d\d" + " - " + prefix + r"\d\d\d\d\d" + r"$", file_name_no_extension)
        if match:
            valid = True
        else:
            valid = False
    else:
        valid = False
    return valid

def is_valid_dated_picture_file_name(file_path):
    valid = True
    date = None
    file_name = file.get_file_name(file_path)
    match = re.match(r"^IMG_(\d\d\d\d\d\d\d\d_\d\d\d\d\d\d).*$", file_name)
    if match:
        valid = True
        date = datetime.datetime.strptime(match.group(1), r'%Y%m%d_%H%M%S')
    else:
        valid = False
    return (valid, date)

def get_date_from_picture_file_name(file_path):
    (_, date) = is_valid_dated_picture_file_name(file_path)
    return date

def get_picture_file_date(file_path, use_folder_date):
    file_date_valid = False
    file_date = None
    if exif_picture_file.has_exif_date_time_original(file_path):
        file_date_valid = True
        file_date = exif_picture_file.get_exif_date_time_original(file_path)
    elif exif_picture_file.has_exif_date_time_digitized(file_path):
        file_date_valid = True
        file_date = exif_picture_file.get_exif_date_time_digitized(file_path)
    else:
        (file_date_valid, file_date) = is_valid_dated_picture_file_name(file_path)
        if not file_date_valid and use_folder_date:
            (folder_date_valid, folder_date) = multimedia_file.has_valid_dated_folder_name(file_path)
            if folder_date_valid:
                file_date_valid = True
                file_date = folder_date
    return (file_date_valid, file_date)

def set_picture_file_date(file_path, file_date):
    exif_picture_file.set_exif_date_time_original(file_path, file_date)
    exif_picture_file.set_exif_date_time_digitized(file_path, file_date)
    file.set_file_date(file_path, file_date)

def check_picture_file_date(file_path, use_folder_date):
    (file_date_valid, file_date) = get_picture_file_date(file_path, use_folder_date)
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
    
def set_picture_file_dates(folder_path, picture_file_names, use_folder_date, process, verbose):
    files_processed = False
    files_process_comments = {}

    for picture_file_name in picture_file_names:
        picture_file_path = os.path.join(folder_path, picture_file_name)
        (file_date_valid, file_date) = get_picture_file_date(picture_file_path, use_folder_date)
        if file_date_valid:
            (folder_date_valid, folder_date) = multimedia_file.has_valid_dated_folder_name(picture_file_path)
            if folder_date_valid:
                if file_date.year == folder_date.year and file_date.month == folder_date.month and file_date.day == folder_date.day:
                    if process:
                        set_picture_file_date(picture_file_path, file_date)
                    if not process or verbose:
                        file_messages.add_file_message(files_process_comments, picture_file_path, "Set File date (" + str(file_date) + ")")
                else:
                    file_messages.add_file_message(files_process_comments, picture_file_path, "File date (" + str(file_date) + ") not matching dated folder (" + folder_path + ")")
            else:
                (folder_period_date_valid, folder_period_beginning_date, folder_period_end_date) = multimedia_file.has_valid_period_dated_folder_name(picture_file_path)
                if folder_period_date_valid:
                    if folder_period_beginning_date <= file_date and file_date <= folder_period_end_date:
                        if process:
                            set_picture_file_date(picture_file_path, file_date)
                        if not process or verbose:
                            file_messages.add_file_message(files_process_comments, picture_file_path, "Set File date (" + str(file_date) + ")")
                    else:
                        file_messages.add_file_message(files_process_comments, picture_file_path, "File date (" + str(file_date) + ") not matching dated folder (" + folder_path + ")")
        else:
            file_messages.add_file_message(files_process_comments, picture_file_path, "No date identified for file")

    files_processed = process
    return (files_processed, files_process_comments)

def move_picture_files(folder_path, picture_file_names, use_folder_date, process, verbose):
    files_processed = False
    files_process_comments = {}
    regular_file_names = [f for f in picture_file_names if multimedia_file.is_valid_file_name(os.path.join(folder_path, f), PICTURE_EXTENSION_PREFIX)]
    panorama_file_names = [f for f in picture_file_names if is_valid_panorama_file_name(os.path.join(folder_path, f))]
    other_file_names = [f for f in picture_file_names if f not in regular_file_names and f not in panorama_file_names]
    if not is_panorama_folder_name(os.path.basename(folder_path)):
        if len(panorama_file_names) > 0:
            # Move panorama files to Panorama folder
            panorama_folder_path = os.path.join(folder_path, PANORAMA_FOLDER_NAME)
            for panorama_file_name in panorama_file_names:
                panorama_file_path = os.path.join(folder_path, panorama_file_name)
                if process:
                    file.move_file_to_folder(panorama_file_path, panorama_folder_path)
                if not process or verbose:
                    file_messages.add_file_message(files_process_comments, panorama_file_path, "Move to " + panorama_folder_path)
    else:
        if len(panorama_file_names) > 0:
            # Move regular files and other files to parent folder
            parent_folder_path = file.get_folder_path(folder_path)
            for panorama_file_name in (regular_file_names + other_file_names):
                panorama_file_path = os.path.join(folder_path, panorama_file_name)
                if process:
                    file.move_file_to_folder(panorama_file_path, parent_folder_path)
                if not process or verbose:
                    file_messages.add_file_message(files_process_comments, panorama_file_path, "Move to " + parent_folder_path)
    
    # Create daily folders
    for picture_file_name in (regular_file_names + other_file_names):
        picture_file_path = os.path.join(folder_path, picture_file_name)
        (file_date_valid, file_date) = get_picture_file_date(picture_file_path, use_folder_date)
        if file_date_valid:
            (folder_date_valid, folder_date) = multimedia_file.has_valid_dated_folder_name(picture_file_path)
            if folder_date_valid:
                if not (file_date.year == folder_date.year and file_date.month == folder_date.month and file_date.day == folder_date.day):
                    dated_folder_name = "{:0>4d}-{:0>2d}-{:0>2d} - XXX".format(file_date.year, file_date.month, file_date.day)
                    dated_folder_path = os.path.join(folder_path, dated_folder_name)
                    if process:
                        file.move_file_to_folder(picture_file_path, dated_folder_path)
                    if not process or verbose:
                        file_messages.add_file_message(files_process_comments, picture_file_path, "Move to " + dated_folder_path)
            else:
                (folder_period_date_valid, folder_period_beginning_date, folder_period_end_date) = multimedia_file.has_valid_period_dated_folder_name(picture_file_path)
                if folder_period_date_valid:
                    if folder_period_beginning_date <= file_date and file_date <= folder_period_end_date:
                        dated_folder_name = "{:0>4d}-{:0>2d}-{:0>2d} - XXX".format(file_date.year, file_date.month, file_date.day)
                        dated_folder_path = os.path.join(folder_path, dated_folder_name)
                        if process:
                            file.move_file_to_folder(picture_file_path, dated_folder_path)
                        if not process or verbose:
                            file_messages.add_file_message(files_process_comments, picture_file_path, "Move to " + dated_folder_path)
        else:
            file_messages.add_file_message(files_process_comments, picture_file_path, "No date identified for file")
    files_processed = process
    return (files_processed, files_process_comments)

def rename_picture_files(folder_path, file_names, use_folder_date, process, verbose):
    files_processed = False
    files_process_comments = {}

    def get_timestamp(file_name):
        timestamp = None
        file_path = os.path.join(folder_path, file_name)
        (file_date_valid, file_date) = get_picture_file_date(file_path, use_folder_date)
        if file_date_valid:
            timestamp = file_date.timestamp()
        else:
            timestamp = 0
        return timestamp

    file_names.sort(key=get_timestamp)

    for index, file_name in enumerate(file_names):
        file_path = os.path.join(folder_path, file_name)
        file_extension = file.get_file_extension(file_path)
        new_file_name = PICTURE_PREFIX + "{:0>5d}".format(index + 1) + file_extension
        if not os.path.exists(file_path):
            file_path += ".tmp"
        if not file_name == new_file_name:
            new_file_path = os.path.join(folder_path, new_file_name)
            if not os.path.exists(new_file_path):
                file_messages.add_file_message(files_process_comments, file_path, "Renamed " + new_file_name)
            else:
                file_messages.add_file_message(files_process_comments, new_file_path, "Renamed " + file_name + ".tmp")
                file_messages.add_file_message(files_process_comments, file_path, "Renamed " + new_file_path)

    files_processed = process
    return (files_processed, files_process_comments)

def process_picture_files_in_folder(folder_path, file_names, use_folder_date, set_dates, move_files, rename_files, process, verbose):
    files_processed = False
    files_process_comments = {}

    picture_file_names = [f for f in file_names if file.get_file_extension(os.path.join(folder_path, f)) in PICTURE_EXTENSION_PREFIX]
    if len(picture_file_names) > 0:
        if set_dates:
            # Change file dates
            (picture_file_dates_processed, picture_file_dates_process_comments) = set_picture_file_dates(folder_path, picture_file_names, use_folder_date, process, verbose)
            files_processed &= picture_file_dates_processed
            file_messages.add_file_messages(files_process_comments, picture_file_dates_process_comments)
        if move_files:
            # Move files
            (move_picture_files_processed, move_picture_files_process_comments) = move_picture_files(folder_path, picture_file_names, use_folder_date, process, verbose)
            files_processed &= move_picture_files_processed
            file_messages.add_file_messages(files_process_comments, move_picture_files_process_comments)
        if rename_files:
            # Rename files
            (rename_picture_files_processed, rename_picture_files_process_comments) = rename_picture_files(folder_path, picture_file_names, use_folder_date, process, verbose)
            files_processed &= rename_picture_files_processed
            file_messages.add_file_messages(files_process_comments, rename_picture_files_process_comments)
    
    return (files_processed, files_process_comments)
