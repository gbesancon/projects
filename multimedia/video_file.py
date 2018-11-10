import file
import file_messages
import multimedia_file
import os
import re
import datetime
import metadata_utility

OLD_PREFIX = "v"
PREFIX = "VID"
EXTENSIONS = [ 
    ".3gp",
    ".avi",
    ".flv",
    ".mov",
    ".mp4",
    ".mpeg",
    ".mpg",
    ".mts",
    ".ogv",
    ".wmv"
]

def is_file(file_path):
    file_extension = file.get_file_extension(file_path)
    return file_extension in EXTENSIONS

def generate_file_name(file_path, use_folder_date):
    file_name = None
    (file_date_valid, file_date) = get_file_date(file_path, use_folder_date)
    if file_date_valid:
        file_extension = file.get_file_extension(file_path)
        file_name = file_date.strftime(PREFIX + "_" + "%Y%m%d_%H%M%S" + file_extension)
    if not file_name:
        file_extension = file.get_file_extension(file_path)
        file_name = PREFIX + file_extension
    return file_name
    
def check_file_name(file_path):
    return multimedia_file.check_multimedia_file_name(file_path, PREFIX)

def get_file_date(file_path, use_folder_date):
    (file_date_valid, file_date, _) = check_file_name(file_path)
    if metadata_utility.has_metadata_key(file_path, metadata_utility.CREATION_DATE):
        file_date_valid = True
        file_date = metadata_utility.get_metadata_value(file_path, metadata_utility.CREATION_DATE)
    elif metadata_utility.has_metadata_key(file_path, metadata_utility.DATE_TIME_ORIGINAL):
        file_date_valid = True
        file_date = metadata_utility.get_metadata_value(file_path, metadata_utility.DATE_TIME_ORIGINAL)
    elif metadata_utility.has_metadata_key(file_path, metadata_utility.DATE_TIME_DIGITIZED):
        file_date_valid = True
        file_date = metadata_utility.get_metadata_value(file_path, metadata_utility.DATE_TIME_DIGITIZED)
    else:
        (file_date_valid, file_date, _) = check_file_name(file_path)
        if not file_date_valid and use_folder_date:
            (folder_date_valid, folder_date) = multimedia_file.has_valid_dated_folder_name(file_path)
            if folder_date_valid:
                file_date_valid = True
                file_date = folder_date
    return (file_date_valid, file_date)

def set_file_date(file_path, file_date):
    file.set_file_date(file_path, file_date)
    
def check_file_date(file_path, use_folder_date):
    (_, file_date) = get_file_date(file_path, use_folder_date)
    return multimedia_file.check_multimedia_file_date(file_path, file_date)  

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
    if rename_files:
        # Rename files
        (rename_file_processed, rename_file_process_comment) = process_rename_file(file_path, use_folder_date, process, verbose)
        file_processed &= rename_file_processed
        file_process_comments.append(rename_file_process_comment)
    return (file_processed, file_process_comments)
