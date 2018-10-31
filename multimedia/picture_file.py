import file
import re
import os
import piexif
import datetime
import file_messages
import multimedia_file
import exif_picture_file

OLD_PICTURE_PREFIX = "p"
REGULAR_PICTURE_PREFIX = "IMG"
PANORAMA_PICTURE_PREFIX = "PAN"
NON_EXIF_PICTURE_EXTENSION = [    
    ".bmp",
    ".gif",
    ".orf",
]
PICTURE_EXTENSION = NON_EXIF_PICTURE_EXTENSION + exif_picture_file.EXIF_PICTURE_EXTENSION

PANORAMA_FOLDER_NAME = "Panorama"

def is_panorama_picture_folder_name(folder_name):
    return folder_name == PANORAMA_FOLDER_NAME

def is_panorama_folder_path(folder_path):
    folder_name = os.path.basename(folder_path)
    return is_panorama_picture_folder_name(folder_name)

def is_picture_file(file_path):
    file_extension = file.get_file_extension(file_path)
    return file_extension in PICTURE_EXTENSION

def generate_picture_file_name(file_path, use_folder_date):
    file_name = None
    (file_date_valid, file_date) = get_picture_file_date(file_path, use_folder_date)
    if file_date_valid:
        folder_path = file.get_folder_path(file_path)
        prefix = PANORAMA_PICTURE_PREFIX if is_panorama_folder_path(folder_path) else REGULAR_PICTURE_PREFIX
        file_extension = file.get_file_extension(file_path)
        file_name = file_date.strftime(prefix + "_%Y%m%d_%H%M%S" + file_extension)
    return file_name

def check_regular_picture_file_name(file_path):
    return multimedia_file.check_multimedia_file_name(file_path, REGULAR_PICTURE_PREFIX)

def is_valid_regular_picture_file_name(file_path):
    (valid, _, _) = check_regular_picture_file_name(file_path)
    return valid

def check_panorama_picture_file_name(file_path):
    return multimedia_file.check_multimedia_file_name(file_path, PANORAMA_PICTURE_PREFIX)

def is_valid_panorama_picture_file_name(file_path):
    (valid, _, _) = check_panorama_picture_file_name(file_path)
    return valid
    
def check_picture_file_name(file_path):
    valid = False
    file_date = None
    error_message = None
    folder_name = file.get_folder_name(file_path)
    if not is_panorama_picture_folder_name(folder_name):
        (valid, file_date, error_message) = check_regular_picture_file_name(file_path)
        if not valid:
            (valid, file_date, error_message) = check_panorama_picture_file_name(file_path)
            if valid:
                valid = False
                file_date = None
                error_message = "Panorama file not stored in a Panorama folder"
            else:
                valid = False
                file_date = None
                error_message = "Filename incorrect"
    else:
        (valid, file_date, error_message) = check_panorama_picture_file_name(file_path)
        if not valid:
            (valid, file_date, error_message) = check_regular_picture_file_name(file_path)
            if valid:
                valid = False
                file_date = None
                error_message = "File should not be stored in a Panorama folder"
            else:
                valid = False
                file_date = None
                error_message = "Filename incorrect"
    return (valid, file_date, error_message)

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
        (file_date_valid, file_date, _) = check_picture_file_name(file_path)
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
    regular_picture_file_names = [f for f in picture_file_names if is_valid_regular_picture_file_name(os.path.join(folder_path, f))]
    panorama_picture_file_names = [f for f in picture_file_names if is_valid_panorama_picture_file_name(os.path.join(folder_path, f))]
    other_picture_file_names = [f for f in picture_file_names if f not in regular_picture_file_names and f not in panorama_picture_file_names]
    if not is_panorama_picture_folder_name(os.path.basename(folder_path)):
        if len(panorama_picture_file_names) > 0:
            # Move panorama files to Panorama folder
            panorama_picture_folder_path = os.path.join(folder_path, PANORAMA_FOLDER_NAME)
            for panorama_picture_file_name in panorama_picture_file_names:
                panorama_picture_file_path = os.path.join(folder_path, panorama_picture_file_name)
                if process:
                    file.move_file_to_folder(panorama_picture_file_path, panorama_picture_folder_path)
                if not process or verbose:
                    file_messages.add_file_message(files_process_comments, panorama_picture_file_path, "Move to " + panorama_picture_folder_path)
    else:
        if len(panorama_picture_file_names) > 0:
            # Move regular files and other files to parent folder
            parent_folder_path = file.get_folder_path(folder_path)
            for panorama_picture_file_name in (regular_picture_file_names + other_picture_file_names):
                panorama_picture_file_path = os.path.join(folder_path, panorama_picture_file_name)
                if process:
                    file.move_file_to_folder(panorama_picture_file_path, parent_folder_path)
                if not process or verbose:
                    file_messages.add_file_message(files_process_comments, panorama_picture_file_path, "Move to " + parent_folder_path)
    
    # Create daily folders
    for picture_file_name in (regular_picture_file_names + other_picture_file_names):
        picture_file_path = os.path.join(folder_path, picture_file_name)
        (file_date_valid, file_date) = get_picture_file_date(picture_file_path, use_folder_date)
        if file_date_valid:
            (folder_date_valid, folder_date) = multimedia_file.has_valid_dated_folder_name(picture_file_path)
            if folder_date_valid:
                if not (file_date.year == folder_date.year and file_date.month == folder_date.month and file_date.day == folder_date.day):
                    dated_folder_name = "{:0>4d}-{:0>2d}-{:0>2d} - XXX".format(file_date.year, file_date.month, file_date.day)
                    parent_folder_path = file.get_folder_path(folder_path)
                    dated_folder_path = os.path.join(parent_folder_path, dated_folder_name)
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

def rename_picture_files(folder_path, picture_file_names, use_folder_date, process, verbose):
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

    picture_file_names.sort(key=get_timestamp)

    for index, file_name in enumerate(picture_file_names):
        file_name_without_extension = os.path.splitext(file_name)[0]
        file_path = os.path.join(folder_path, file_name)
        new_file_name = generate_picture_file_name(file_path, use_folder_date)
        if not new_file_name:
            file_extension = file.get_file_extension(file_path)
            new_file_name = OLD_PICTURE_PREFIX + "{:0>5d}".format(index + 1) + file_extension
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
                    file_messages.add_file_message(files_process_comments, file_path, "Renamed " + new_file_name)

    files_processed = process
    return (files_processed, files_process_comments)

def process_picture_files_in_folder(folder_path, file_names, use_folder_date, set_dates, move_files, rename_files, process, verbose):
    files_processed = False
    files_process_comments = {}

    picture_file_names = [f for f in file_names if file.get_file_extension(os.path.join(folder_path, f)) in PICTURE_EXTENSION]
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
