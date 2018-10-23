import file
import file_messages
import multimedia_file
import os

AUDIO_PREFIX = "a"
AUDIO_EXTENSION_PREFIX = {    
    ".mp3" : AUDIO_PREFIX,
    ".wav" : AUDIO_PREFIX,
    ".ogg" : AUDIO_PREFIX,
}

def is_audio_file(file_path):
    file_extension = file.get_file_extension(file_path)
    return file_extension in AUDIO_EXTENSION_PREFIX

def set_audio_file_date(file_path, file_date):
    file.set_file_date(file_path, file_date)
    
def check_audio_file_date(file_path):
    (_, file_date) = multimedia_file.get_multimedia_file_date(file_path, None)
    return multimedia_file.check_multimedia_file_date(file_path, file_date)
    
def set_audio_file_dates(folder_path, audio_file_names, process, verbose):
    files_processed = False
    files_process_comments = {}

    for audio_file_name in audio_file_names:
        audio_file_path = os.path.join(folder_path, audio_file_name)
        (file_date_valid, file_date) = multimedia_file.get_multimedia_file_date(audio_file_path, None)
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

def rename_audio_files(folder_path, audio_file_names, process, verbose):
    files_processed = False
    files_process_comments = {}
    
    files_processed = process
    return (files_processed, files_process_comments)

def process_audio_files_in_folder(folder_path, file_names, process, verbose):
    files_processed = False
    files_process_comments = {}

    audio_file_names = [f for f in file_names if file.get_file_extension(os.path.join(folder_path, f)) in AUDIO_EXTENSION_PREFIX]
    if len(audio_file_names) > 0:
        # Change file dates
        (audio_file_dates_processed, audio_file_dates_process_comments) = set_audio_file_dates(folder_path, audio_file_names, process, verbose)
        files_processed &= audio_file_dates_processed
        file_messages.add_file_messages(files_process_comments, audio_file_dates_process_comments)
        # Rename files
        (rename_audio_files_processed, rename_audio_files_process_comments) = rename_audio_files(folder_path, audio_file_names, process, verbose)
        files_processed &= rename_audio_files_processed
        file_messages.add_file_messages(files_process_comments, rename_audio_files_process_comments)
    
    files_processed = process
    return (files_processed, files_process_comments)