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

def check_file_extension(file_path):
    file_extension_valid = True
    file_extension_error_message = None
    file_extension = file.get_file_extension(file_path)
    if not file_extension in EXTENSION_PREFIX:
        file_extension_valid = False
        file_extension_error_message = "Extension " + file.get_file_extension(file_path) + " not supported"
    return (file_extension_valid, file_extension, file_extension_error_message)

def check_file_location(file_path, use_folder_date):
    file_location_valid = True
    file_location_error_message = None
    def get_file_date(file_path):
        file_date = None
        if audio_file.is_audio_file(file_path):
            (file_date_valid, file_date) = audio_file.get_audio_file_date(file_path, use_folder_date)
        elif picture_file.is_picture_file(file_path):
            (file_date_valid, file_date) = picture_file.get_picture_file_date(file_path, use_folder_date)
        elif video_file.is_video_file(file_path):
            (file_date_valid, file_date) = video_file.get_video_file_date(file_path, use_folder_date)
        if not file_date_valid:
            file_date = None
        return file_date
    (file_location_valid, file_date, file_location_error_message) = file.check_file_dates(file_path, "File date", get_file_date, "Folder date", multimedia_file.get_date_from_folder_name, 24*60*60)
    if not file_location_valid:
        if file_date:
            file_location_error_message = "File date (" + str(file_date) + ") shouldn't be stored in folder " + file.get_folder_path(file_path)
        else:
            file_location_error_message = "File date (undefined) shouldn't be stored in folder " + file.get_folder_path(file_path)
    return (file_location_valid, file_location_error_message)

def check_file(file_path, use_folder_date, verbose):
    (file_valid, _, file_error_message) = check_file_extension(file_path)
    if file_valid:
        (file_valid, file_error_message) = check_file_location(file_path, use_folder_date)
        if file_valid:
            if audio_file.is_audio_file(file_path):
                check_file_name = audio_file.check_audio_file_name
                check_file_date = audio_file.check_audio_file_date
            elif picture_file.is_picture_file(file_path):
                check_file_name = picture_file.check_picture_file_name
                check_file_date = picture_file.check_picture_file_date
            elif video_file.is_video_file(file_path):
                check_file_name = video_file.check_video_file_name
                check_file_date = video_file.check_video_file_date
            (file_valid, file_error_message) = check_file_name(file_path)
            if file_valid:
                (file_valid, _, file_error_message) = check_file_date(file_path, use_folder_date)
    return (file_valid, file_error_message)

def check_files_in_folder(folder_path, file_names, use_folder_date, verbose):
    files_valid = True
    files_errors = {}
    for file_name in file_names:
        file_path = os.path.join(folder_path, file_name)
        (file_valid, file_error_message) = check_file(file_path, use_folder_date, verbose)
        files_valid &= file_valid
        if file_valid:
            if verbose:
                file_messages.add_file_message(files_errors, file_path, "Checked")
        else:
            file_messages.add_file_message(files_errors, file_path, file_error_message)
    return (files_valid, files_errors)

def process_files_in_folder(folder_path, file_names, use_folder_date, set_dates, move_files, rename_files, process, verbose):
    files_processed = False
    files_process_comments = {}

    (audio_files_processed, audio_files_process_comments) = audio_file.process_audio_files_in_folder(folder_path, file_names, use_folder_date, set_dates, move_files, rename_files, process, verbose)
    files_processed &= audio_files_processed
    file_messages.add_file_messages(files_process_comments, audio_files_process_comments)

    (picture_files_processed, picture_files_process_comments) = picture_file.process_picture_files_in_folder(folder_path, file_names, use_folder_date, set_dates, move_files, rename_files, process, verbose)
    files_processed &= picture_files_processed
    file_messages.add_file_messages(files_process_comments, picture_files_process_comments)
    
    (video_files_processed, video_files_process_comments) = video_file.process_video_files_in_folder(folder_path, file_names, use_folder_date, set_dates, move_files, rename_files, process, verbose)
    files_processed &= video_files_processed
    file_messages.add_file_messages(files_process_comments, video_files_process_comments)
            
    return (files_processed, files_process_comments)

def check_and_process_files_in_folder(folder_path, use_folder_date, set_dates, move_files, rename_files, process, verbose):
    folder_valid = True
    folder_check_errors = {}
    folder_process_comments = {}
    (folder_date_valid, _) = multimedia_file.has_valid_dated_folder_name(os.path.join(folder_path, ".tmp"))
    if folder_date_valid:
        for path, _, files in os.walk(folder_path):
            if len(files):
                (files_valid, _) = check_files_in_folder(path, files, use_folder_date, verbose)
                folder_valid &= files_valid
                if not files_valid:
                    (files_processed, files_process_comments) = process_files_in_folder(path, files, use_folder_date, set_dates, move_files, rename_files, process, verbose)
                    folder_valid &= files_processed
                    file_messages.add_file_messages(folder_process_comments, files_process_comments)
                    (files_valid, files_check_errors) = check_files_in_folder(path, files, use_folder_date, verbose)
                    folder_valid &= files_valid
                    file_messages.add_file_messages(folder_check_errors, files_check_errors)
    else:
        folder_valid &= folder_date_valid
        file_messages.add_file_message(folder_check_errors, folder_path, "Folder date undefined")
    return (folder_valid, folder_check_errors, folder_process_comments)

def get_sub_folders_with_files(folder_path):
    sub_folder_pathes = []
    if os.path.exists(folder_path):
        for path, _, files in os.walk(folder_path):
            if len(files) > 0:
                sub_folder_pathes += [path]
    return sub_folder_pathes

def check_and_process_files_in_folders(folder_pathes, use_folder_date, set_dates, move_files, rename_files, process, verbose):
    valid = True
    check_errors = {}
    process_comments = {}
    multimedia_folder_pathes = []
    # Generate a flat list of all folders to process
    for folder_path in folder_pathes:
        multimedia_folder_pathes += get_sub_folders_with_files(folder_path)
    for multimedia_folder_path in multimedia_folder_pathes:
        (folder_valid, folder_check_errors, folder_process_comments) = check_and_process_files_in_folder(multimedia_folder_path, use_folder_date, set_dates, move_files, rename_files, process, verbose)
        valid &= folder_valid
        if not folder_valid or verbose:
            file_messages.print_file_messages(folder_check_errors)
        file_messages.print_file_messages(folder_process_comments)
        file_messages.add_file_messages(check_errors, folder_check_errors)
        file_messages.add_file_messages(process_comments, folder_process_comments)
    return (valid, check_errors, process_comments)

def main(argv):
    parser = argparse.ArgumentParser()
    parser.add_argument('-d', '--directory', nargs='+', help="Directory", required=True)
    parser.add_argument('--set-dates', help="Set dates of files", default=False, action="store_true")
    parser.add_argument('--use-folder-date', help="Use folder date if no date found for the file", default=False, action="store_true")
    parser.add_argument('-m', '--move-files', help="Move files", default=False, action="store_true")
    parser.add_argument('-r', '--rename-files', help="Rename files", default=False, action="store_true")
    parser.add_argument('-p', '--process', help="Process", default=False, action="store_true")
    parser.add_argument('-v', '--verbose', help="Verbose", default=False, action="store_true")
    args = parser.parse_args(argv)
    (valid, _, _) = check_and_process_files_in_folders(args.directory, args.use_folder_date, args.set_dates, args.move_files, args.rename_files, args.process, args.verbose)
    return valid

if __name__ == "__main__":
    main(sys.argv[1:])