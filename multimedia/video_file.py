import file
import file_messages
import multimedia_file
import os
import re
import datetime

OLD_VIDEO_PREFIX = "v"
VIDEO_PREFIX = "VID"
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

def is_video_file(file_path):
    file_extension = file.get_file_extension(file_path)
    return file_extension in VIDEO_EXTENSION

def generate_video_file_name(file_path, use_folder_date):
    file_name = None
    (file_date_valid, file_date) = get_video_file_date(file_path, use_folder_date)
    if file_date_valid:
        file_extension = file.get_file_extension(file_path)
        file_name = file_date.strftime(VIDEO_PREFIX + "_" + "%Y%m%d_%H%M%S" + file_extension)
    return file_name
    
def check_video_file_name(file_path):
    return multimedia_file.check_multimedia_file_name(file_path, VIDEO_PREFIX)

def set_video_file_date(file_path, file_date):
    file.set_file_date(file_path, file_date)
    
def check_video_file_date(file_path, use_folder_date):
    (_, file_date) = get_video_file_date(file_path, use_folder_date)
    return multimedia_file.check_multimedia_file_date(file_path, file_date)
    
def get_video_file_date(file_path, use_folder_date):
    (file_date_valid, file_date, _) = check_video_file_name(file_path)
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
                        file_messages.add_file_message(files_process_comments, folder_path, video_file_name, "Set File date (" + str(file_date) + ")")
                else:
                    file_messages.add_file_message(files_process_comments, folder_path, video_file_name, "File date (" + str(file_date) + ") not matching dated folder (" + folder_path + ")")
            else:
                pass
        else:
            if folder_date_valid and use_folder_date:
                if process:
                    set_video_file_date(video_file_path, folder_date)
                if not process or verbose:
                    file_messages.add_file_message(files_process_comments, folder_path, video_file_name, "Set File date (" + str(folder_date) + ")")
            else:
                file_messages.add_file_message(files_process_comments, folder_path, video_file_name, "No date identified for file")
                
    files_processed = process
    return (files_processed, files_process_comments)

def move_video_files(folder_path, video_file_names, use_folder_date, process, verbose):
    files_processed = False
    files_process_comments = {}
    # Create daily folders
    for video_file_name in video_file_names:
        video_file_path = os.path.join(folder_path, video_file_name)
        (file_date_valid, file_date) = get_video_file_date(video_file_path, use_folder_date)
        if file_date_valid:
            (folder_date_valid, folder_date) = multimedia_file.has_valid_dated_folder_name(video_file_path)
            if folder_date_valid:
                if not (file_date.year == folder_date.year and file_date.month == folder_date.month and file_date.day == folder_date.day):
                    dated_folder_name = "{:0>4d}-{:0>2d}-{:0>2d} - XXX".format(file_date.year, file_date.month, file_date.day)
                    parent_folder_path = file.get_folder_path(folder_path)
                    dated_folder_path = os.path.join(parent_folder_path, dated_folder_name)
                    if process:
                        file.move_file_to_folder(video_file_path, dated_folder_path)
                    if not process or verbose:
                        file_messages.add_file_message(files_process_comments, folder_path, video_file_name, "Move to " + dated_folder_path)
            else:
                (folder_period_date_valid, folder_period_beginning_date, folder_period_end_date) = multimedia_file.has_valid_period_dated_folder_name(video_file_path)
                if folder_period_date_valid:
                    if folder_period_beginning_date <= file_date and file_date <= folder_period_end_date:
                        dated_folder_name = "{:0>4d}-{:0>2d}-{:0>2d} - XXX".format(file_date.year, file_date.month, file_date.day)
                        dated_folder_path = os.path.join(folder_path, dated_folder_name)
                        if process:
                            file.move_file_to_folder(video_file_path, dated_folder_path)
                        if not process or verbose:
                            file_messages.add_file_message(files_process_comments, folder_path, video_file_name, "Move to " + dated_folder_path)
        else:
            file_messages.add_file_message(files_process_comments, folder_path, video_file_name, "No date identified for file")
    files_processed = process
    return (files_processed, files_process_comments)

def rename_video_files(folder_path, video_file_names, use_folder_date, process, verbose):
    files_processed = False
    files_process_comments = {}

    def get_timestamp(file_name):
        timestamp = None
        file_path = os.path.join(folder_path, file_name)
        (file_date_valid, file_date) = get_video_file_date(file_path, use_folder_date)
        if file_date_valid:
            timestamp = file_date.timestamp()
        else:
            timestamp = 0
        return timestamp

    video_file_names.sort(key=get_timestamp)

    for index, file_name in enumerate(video_file_names):
        file_name_without_extension = os.path.splitext(file_name)[0]
        file_path = os.path.join(folder_path, file_name)
        new_file_name = generate_video_file_name(file_path, use_folder_date)
        if not new_file_name:
            file_extension = file.get_file_extension(file_path)
            new_file_name = OLD_VIDEO_PREFIX + "{:0>5d}".format(index + 1) + file_extension
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
                    file_messages.add_file_message(files_process_comments, folder_path, file_name, "Renamed " + new_file_name)

    files_processed = process
    return (files_processed, files_process_comments)

def process_video_files_in_folder(folder_path, file_names, use_folder_date, set_dates, move_files, rename_files, process, verbose):
    files_processed = False
    files_process_comments = {}
    
    video_file_names = [f for f in file_names if file.get_file_extension(os.path.join(folder_path, f)) in VIDEO_EXTENSION]
    if len(video_file_names) > 0:
        if set_dates:
            # Change file dates
            (video_file_dates_processed, video_file_dates_process_comments) = set_video_file_dates(folder_path, video_file_names, use_folder_date, process, verbose)
            files_processed &= video_file_dates_processed
            file_messages.add_file_messages(files_process_comments, video_file_dates_process_comments)
        if move_files:
            # Move files
            (move_video_files_processed, move_video_files_process_comments) = move_video_files(folder_path, video_file_names, use_folder_date, process, verbose)
            files_processed &= move_video_files_processed
            file_messages.add_file_messages(files_process_comments, move_video_files_process_comments)
        if rename_files:
            # Rename files
            (rename_video_files_processed, rename_video_files_process_comments) = rename_video_files(folder_path, video_file_names, use_folder_date, process, verbose)
            files_processed &= rename_video_files_processed
            file_messages.add_file_messages(files_process_comments, rename_video_files_process_comments)
    
    return (files_processed, files_process_comments)
    