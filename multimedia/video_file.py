import file
import file_messages
import multimedia_file
import os
import re
import datetime

VIDEO_PREFIX = "v"
VIDEO_EXTENSION = [ 
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
VIDEO_EXTENSION_PREFIX = {}
for file_extension in VIDEO_EXTENSION:
    VIDEO_EXTENSION_PREFIX[file_extension] = VIDEO_PREFIX

def is_video_file(file_path):
    file_extension = file.get_file_extension(file_path)
    return file_extension in VIDEO_EXTENSION_PREFIX
    
def check_video_file_name(file_path):
    return multimedia_file.check_multimedia_file_name(file_path, VIDEO_EXTENSION_PREFIX)

def set_video_file_date(file_path, file_date):
    file.set_file_date(file_path, file_date)
    
def check_video_file_date(file_path, use_folder_date):
    (_, file_date) = get_video_file_date(file_path, use_folder_date)
    return multimedia_file.check_multimedia_file_date(file_path, file_date)
    
def is_valid_dated_video_file_name(file_path):
    valid = True
    date = None
    file_name = file.get_file_name(file_path)
    match = re.match(r"^VID_(\d\d\d\d\d\d\d\d_\d\d\d\d\d\d).*$", file_name)
    if match:
        valid = True
        date = datetime.datetime.strptime(match.group(1), r'%Y%m%d_%H%M%S')
    else:
        valid = False
    return (valid, date)

def get_date_from_video_file_name(file_path):
    (_, date) = is_valid_dated_video_file_name(file_path)
    return date

def get_video_file_date(file_path, use_folder_date):
    (file_date_valid, file_date) = is_valid_dated_video_file_name(file_path)
    if not file_date_valid and use_folder_date:
        (folder_date_valid, folder_date) = multimedia_file.has_valid_dated_folder_name(file_path)
        if folder_date_valid:
            file_date_valid = True
            file_date = folder_date
    return (file_date_valid, file_date)

def set_video_file_dates(folder_path, video_file_names, use_folder_date, process, verbose):
    files_processed = False
    files_process_comments = {}

    for video_file_name in video_file_names:
        video_file_path = os.path.join(folder_path, video_file_name)
        (file_date_valid, file_date) = get_video_file_date(video_file_path, use_folder_date)
        (folder_date_valid, folder_date) = multimedia_file.has_valid_dated_folder_name(video_file_path)
        if file_date_valid:
            if folder_date_valid:
                if file_date.year == folder_date.year and file_date.month == folder_date.month and file_date.day == folder_date.day:
                    if process:
                        set_video_file_date(video_file_path, file_date)
                    if not process or verbose:
                        file_messages.add_file_message(files_process_comments, video_file_path, "Set File date (" + str(file_date) + ")")
                else:
                    file_messages.add_file_message(files_process_comments, video_file_path, "File date (" + str(file_date) + ") not matching dated folder (" + folder_path + ")")
            else:
                pass
        else:
            if folder_date_valid:
                if process:
                    set_video_file_date(video_file_path, folder_date)
                if not process or verbose:
                    file_messages.add_file_message(files_process_comments, video_file_path, "Set File date (" + str(folder_date) + ")")
            else:
                file_messages.add_file_message(files_process_comments, video_file_path, "No date identified for file")
                
    files_processed = process
    return (files_processed, files_process_comments)

def move_video_files(folder_path, video_file_names, process, verbose):
    files_processed = False
    files_process_comments = {}
    
    files_processed = process
    return (files_processed, files_process_comments)

def rename_video_files(folder_path, video_file_names, process, verbose):
    files_processed = False
    files_process_comments = {}
    
    files_processed = process
    return (files_processed, files_process_comments)

def process_video_files_in_folder(folder_path, file_names, use_folder_date, set_dates, move_files, rename_files, process, verbose):
    files_processed = False
    files_process_comments = {}
    
    video_file_names = [f for f in file_names if file.get_file_extension(os.path.join(folder_path, f)) in VIDEO_EXTENSION_PREFIX]
    if len(video_file_names) > 0:
        if set_dates:
            # Change file dates
            (video_file_dates_processed, video_file_dates_process_comments) = set_video_file_dates(folder_path, video_file_names, use_folder_date, process, verbose)
            files_processed &= video_file_dates_processed
            file_messages.add_file_messages(files_process_comments, video_file_dates_process_comments)
        if move_files:
            # Move files
            (move_video_files_processed, move_video_files_process_comments) = move_video_files(folder_path, video_file_names, process, verbose)
            files_processed &= move_video_files_processed
            file_messages.add_file_messages(files_process_comments, move_video_files_process_comments)
        if rename_files:
            # Rename files
            (rename_video_files_processed, rename_video_files_process_comments) = rename_video_files(folder_path, video_file_names, process, verbose)
            files_processed &= rename_video_files_processed
            file_messages.add_file_messages(files_process_comments, rename_video_files_process_comments)
    
    return (files_processed, files_process_comments)
    