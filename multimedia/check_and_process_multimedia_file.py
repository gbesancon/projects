from __future__ import print_function
import argparse
import os
import platform
import time
import datetime
import sys
import re
import piexif
import file_messages
import audio_file
import picture_file
import video_file
import file
import multimedia_file

EXTENSION_PREFIX = {}
for d in [audio_file.AUDIO_EXTENSION_PREFIX, picture_file.PICTURE_EXTENSION_PREFIX, video_file.VIDEO_EXTENSION_PREFIX]:
    EXTENSION_PREFIX.update(d)

def check_file_date(file_path):
    file_date_valid = False
    file_date = None
    file_error_message = None
    if audio_file.is_audio_file(file_path):
        (file_date_valid, file_date, file_error_message) = audio_file.check_audio_file_date(file_path)
    elif picture_file.is_picture_file(file_path):
        (file_date_valid, file_date, file_error_message) = picture_file.check_picture_file_date(file_path)
    elif video_file.is_video_file(file_path):
        (file_date_valid, file_date, file_error_message) = video_file.check_video_file_date(file_path)
    return (file_date_valid, file_date, file_error_message)

def check_file_name(file_path):
    valid = False
    error_message = None
    folder_name = file.get_folder_name(file_path)
    if multimedia_file.has_valid_extension(file_path, EXTENSION_PREFIX):
        if multimedia_file.is_valid_file_name(file_path, EXTENSION_PREFIX):
            if not picture_file.is_panorama_folder_name(folder_name):
                valid = True
            else:
                valid = False
                error_message = "File should not be stored in a Panorama folder"
        else:
            if picture_file.is_valid_panorama_file_name(file_path):
                if picture_file.is_panorama_folder_name(folder_name):
                    valid = True
                else:
                    valid = False
                    error_message = "Panorama file not stored in a Panorama folder"
            else:
                if picture_file.is_panorama_folder_name(folder_name):
                    valid = False
                    error_message = "Panorama filename incorrect"
                else:
                    valid = False
                    error_message = "Filename incorrect"
    else:
        valid = False
        error_message = "Extension " + file.get_file_extension(file_path) + " not supported"
    return (valid, error_message)

def check_file(file_path, verbose):
    (file_valid, file_error_message) = check_file_name(file_path)
    if file_valid:
        (file_valid, _, file_error_message) = check_file_date(file_path)
    return (file_valid, file_error_message)

def check_and_process_sub_folders_in_folder(folder_path, sub_folder_names, process, verbose):
    sub_folders_valid = True
    sub_folders_check_errors = {}
    sub_folders_process_comments = {}
    for sub_folder_name in sub_folder_names:
        sub_folder_path = os.path.join(folder_path, sub_folder_name)
        (sub_folder_valid, sub_folder_check_errors, sub_folder_process_comments) = check_and_process_files_and_sub_folders_in_folder(sub_folder_path, process, verbose)
        sub_folders_valid &= sub_folder_valid
        file_messages.add_file_messages(sub_folders_check_errors, sub_folder_check_errors)
        file_messages.add_file_messages(sub_folders_process_comments, sub_folder_process_comments)
    return (sub_folders_valid, sub_folders_check_errors, sub_folders_process_comments)

def check_files_in_folder(folder_path, file_names, verbose):
    files_valid = True
    files_errors = {}
    for file_name in file_names:
        file_path = os.path.join(folder_path, file_name)
        (file_valid, file_error_message) = check_file(file_path, verbose)
        files_valid &= file_valid
        if not file_valid:
            file_messages.add_file_message(files_errors, file_path, file_error_message)
        elif verbose:
            file_messages.add_file_message(files_errors, file_path, "Checked")
    return (files_valid, files_errors)

def process_files_in_folder(folder_path, file_names, process, verbose):
    files_processed = False
    files_process_comments = {}

    audio_file_names = [f for f in file_names if file.get_file_extension(os.path.join(folder_path, f)) in audio_file.AUDIO_EXTENSION_PREFIX]
    if len(audio_file_names) > 0:
        (audio_files_processed, audio_files_process_comments) = audio_file.process_audio_files_in_folder(folder_path, audio_file_names, process, verbose)
        files_processed &= audio_files_processed
        file_messages.add_file_messages(files_process_comments, audio_files_process_comments)

    picture_file_names = [f for f in file_names if file.get_file_extension(os.path.join(folder_path, f)) in picture_file.PICTURE_EXTENSION_PREFIX]
    if len(picture_file_names) > 0:
        (picture_files_processed, picture_files_process_comments) = picture_file.process_picture_files_in_folder(folder_path, picture_file_names, process, verbose)
        files_processed &= picture_files_processed
        file_messages.add_file_messages(files_process_comments, picture_files_process_comments)
    
    video_file_names = [f for f in file_names if file.get_file_extension(os.path.join(folder_path, f)) in video_file.VIDEO_EXTENSION_PREFIX]
    if len(video_file_names) > 0:
        (video_files_processed, video_files_process_comments) = video_file.process_video_files_in_folder(folder_path, video_file_names, process, verbose)
        files_processed &= video_files_processed
        file_messages.add_file_messages(files_process_comments, video_files_process_comments)
            
    files_processed = process
    return (files_processed, files_process_comments)

def check_and_process_files_and_sub_folders_in_folder(folder_path, process, verbose):
    folder_valid = True
    folder_check_errors = {}
    folder_process_comments = {}
    if os.path.exists(folder_path):
        for path, dirs, files in os.walk(folder_path):
            if len(dirs) > 0:
                (sub_folders_valid, sub_folders_check_errors, sub_folders_process_comments) = check_and_process_sub_folders_in_folder(path, dirs, process, verbose)
                folder_valid &= sub_folders_valid
                file_messages.add_file_messages(folder_check_errors, sub_folders_check_errors)
                file_messages.add_file_messages(folder_process_comments, sub_folders_process_comments)
            if len(files):
                (files_valid, _) = check_files_in_folder(path, files, verbose)
                folder_valid &= files_valid
                if not files_valid:
                    (files_processed, files_process_comments) = process_files_in_folder(path, files, process, verbose)
                    folder_valid &= files_processed
                    file_messages.add_file_messages(folder_process_comments, files_process_comments)
                    if not files_processed or verbose:
                        file_messages.print_file_messages(files_process_comments)
                    (files_valid, files_check_errors) = check_files_in_folder(path, files, verbose)
                    folder_valid &= files_valid
                    file_messages.add_file_messages(folder_check_errors, files_check_errors)
                    if not files_valid or verbose:
                        file_messages.print_file_messages(files_check_errors)        
    else:
        folder_valid = False
        file_messages.add_file_message(folder_check_errors, folder_path, "Folder doesn't exist.")
    return (folder_valid, folder_check_errors, folder_process_comments)

def check_and_process_files_in_folders(folder_pathes, process, verbose):
    valid = True
    check_errors = {}
    process_comments = {}
    for folder_path in folder_pathes:
        (folder_valid, folder_check_errors, folder_process_comments) = check_and_process_files_and_sub_folders_in_folder(folder_path, process, verbose)
        valid &= folder_valid
        file_messages.add_file_messages(check_errors, folder_check_errors)
        file_messages.add_file_messages(process_comments, folder_process_comments)
    return (valid, check_errors, process_comments)

def main(argv):
    parser = argparse.ArgumentParser()
    parser.add_argument('-d', '--directory', nargs='+', help="Directory", required=True)
    parser.add_argument('-p', '--process', help="Rename files and set dates", default=False, action="store_true")
    parser.add_argument('-v', '--verbose', help="Verbose", default=False, action="store_true")
    args = parser.parse_args(argv)
    (valid, _, _) = check_and_process_files_in_folders(args.directory, args.process, args.verbose)
    return valid

if __name__ == "__main__":
    main(sys.argv[1:])