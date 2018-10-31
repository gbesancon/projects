import file
import file_messages
import multimedia_file
import os
import re
import datetime

OLD_AUDIO_PREFIX = "a"
AUDIO_PREFIX = "AUD"
AUDIO_EXTENSION = [    
    ".mp3",
    ".ogg",
    ".wav"
]

def is_audio_file(file_path):
    file_extension = file.get_file_extension(file_path)
    return file_extension in AUDIO_EXTENSION

def generate_audio_file_name(file_path, use_folder_date):
    file_name = None
    (file_date_valid, file_date) = get_audio_file_date(file_path, use_folder_date)
    if file_date_valid:
        file_extension = file.get_file_extension(file_path)
        file_name = file_date.strftime(AUDIO_PREFIX + "_" + "%Y%m%d_%H%M%S" + file_extension)
    return file_name
    
def check_audio_file_name(file_path):
    return multimedia_file.check_multimedia_file_name(file_path, AUDIO_PREFIX)

def get_audio_file_date(file_path, use_folder_date):
    (file_date_valid, file_date, _) = check_audio_file_name(file_path)
    if not file_date_valid and use_folder_date:
        (folder_date_valid, folder_date) = multimedia_file.has_valid_dated_folder_name(file_path)
        if folder_date_valid:
            file_date_valid = True
            file_date = folder_date
    return (file_date_valid, file_date)

def set_audio_file_date(file_path, file_date):
    file.set_file_date(file_path, file_date)
    
def check_audio_file_date(file_path, use_folder_date):
    (_, file_date) = get_audio_file_date(file_path, use_folder_date)
    return multimedia_file.check_multimedia_file_date(file_path, file_date)

def set_audio_file_dates(folder_path, audio_file_names, use_folder_date, process, verbose):
    files_processed = False
    files_process_comments = {}

    for audio_file_name in audio_file_names:
        audio_file_path = os.path.join(folder_path, audio_file_name)
        (file_date_valid, file_date) = get_audio_file_date(audio_file_path, use_folder_date)
        (folder_date_valid, folder_date) = multimedia_file.has_valid_dated_folder_name(audio_file_path)
        if file_date_valid:
            if folder_date_valid:
                if file_date.year == folder_date.year and file_date.month == folder_date.month and file_date.day == folder_date.day:
                    if process:
                        set_audio_file_date(audio_file_path, file_date)
                    if not process or verbose:
                        file_messages.add_file_message(files_process_comments, audio_file_path, "Set File date (" + str(file_date) + ")")
                else:
                    file_messages.add_file_message(files_process_comments, audio_file_path, "File date (" + str(file_date) + ") not matching dated folder (" + folder_path + ")")
            else:
                pass
        else:
            if folder_date_valid:
                if process:
                    set_audio_file_date(audio_file_path, folder_date)
                if not process or verbose:
                    file_messages.add_file_message(files_process_comments, audio_file_path, "Set File date (" + str(folder_date) + ")")
            else:
                file_messages.add_file_message(files_process_comments, audio_file_path, "No date identified for file")
            
    files_processed = process
    return (files_processed, files_process_comments)

def rename_audio_files(folder_path, audio_file_names, use_folder_date, process, verbose):
    files_processed = False
    files_process_comments = {}

    def get_timestamp(file_name):
        timestamp = None
        file_path = os.path.join(folder_path, file_name)
        (file_date_valid, file_date) = get_audio_file_date(file_path, use_folder_date)
        if file_date_valid:
            timestamp = file_date.timestamp()
        else:
            timestamp = 0
        return timestamp

    audio_file_names.sort(key=get_timestamp)

    for index, file_name in enumerate(audio_file_names):
        file_name_without_extension = os.path.splitext(file_name)[0]
        file_path = os.path.join(folder_path, file_name)
        new_file_name = generate_audio_file_name(file_path, use_folder_date)
        if not new_file_name:
            file_extension = file.get_file_extension(file_path)
            new_file_name = OLD_AUDIO_PREFIX + "{:0>5d}".format(index + 1) + file_extension
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

def process_audio_files_in_folder(folder_path, file_names, use_folder_date, set_dates, move_files, rename_files, process, verbose):
    files_processed = False
    files_process_comments = {}

    audio_file_names = [f for f in file_names if file.get_file_extension(os.path.join(folder_path, f)) in AUDIO_EXTENSION]
    if len(audio_file_names) > 0:
        if set_dates:
            # Change file dates
            (audio_file_dates_processed, audio_file_dates_process_comments) = set_audio_file_dates(folder_path, audio_file_names, use_folder_date, process, verbose)
            files_processed &= audio_file_dates_processed
            file_messages.add_file_messages(files_process_comments, audio_file_dates_process_comments)
        if rename_files:
            # Rename files
            (rename_audio_files_processed, rename_audio_files_process_comments) = rename_audio_files(folder_path, audio_file_names, use_folder_date, process, verbose)
            files_processed &= rename_audio_files_processed
            file_messages.add_file_messages(files_process_comments, rename_audio_files_process_comments)
    
    return (files_processed, files_process_comments)
