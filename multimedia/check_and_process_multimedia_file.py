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

EXTENSIONS = audio_file.EXTENSIONS + picture_file.EXTENSIONS + video_file.EXTENSIONS

def check_file_extension(file_path):
    file_extension_valid = True
    file_extension_error_message = None
    file_extension = file.get_file_extension(file_path)
    if not file_extension in EXTENSIONS:
        file_extension_valid = False
        file_extension_error_message = "Extension " + file.get_file_extension(file_path) + " not supported"
    return (file_extension_valid, file_extension, file_extension_error_message)

def check_file_location(file_path, use_folder_date):
    file_location_valid = True
    file_location_error_message = None
    
    file_date = None
    if audio_file.is_file(file_path):
        (file_date_valid, file_date) = audio_file.get_file_date(file_path, use_folder_date)
    elif picture_file.is_file(file_path):
        (file_date_valid, file_date) = picture_file.get_file_date(file_path, use_folder_date)
    elif video_file.is_file(file_path):
        (file_date_valid, file_date) = video_file.get_file_date(file_path, use_folder_date)
    
    if file_date_valid:
        (folder_date_valid, folder_date) = multimedia_file.has_valid_dated_folder_name(file_path)
        if folder_date_valid:
            (file_location_valid, file_date, file_location_error_message) = file.check_file_dates(file_path, "File date", lambda f : file_date, "Folder date", lambda f : folder_date, 24*60*60)
            if not file_location_valid:
                file_location_error_message = "File date (" + str(file_date) + ") shouldn't be stored in folder " + file.get_folder_path(file_path)
        else:
            (folder_date_valid, folder_beginning_date, folder_end_date) = multimedia_file.has_valid_period_dated_folder_name(file_path)
            if folder_date_valid:
                (file_location_valid, file_date, file_location_error_message) = file.check_file_dates(file_path, "File date", lambda f : file_date, "Folder date", lambda f : folder_beginning_date, folder_end_date.timestamp() - folder_beginning_date.timestamp())
                if not file_location_valid:
                    file_location_error_message = "File date (" + str(file_date) + ") shouldn't be stored in folder " + file.get_folder_path(file_path)
            
            
    else:
        file_location_valid = False
        file_location_error_message = "File date undefined"
    return (file_location_valid, file_location_error_message)

def check_file_name(file_path):
    if audio_file.is_file(file_path):
        check_file_name = audio_file.check_file_name
    elif picture_file.is_file(file_path):
        check_file_name = picture_file.check_file_name
    elif video_file.is_file(file_path):
        check_file_name = video_file.check_file_name
    return check_file_name(file_path)

def check_file_date(file_path, use_folder_date):
    if audio_file.is_file(file_path):
        check_file_date = audio_file.check_file_date
    elif picture_file.is_file(file_path):
        check_file_date = picture_file.check_file_date
    elif video_file.is_file(file_path):
        check_file_date = video_file.check_file_date
    return check_file_date(file_path, use_folder_date)

def check_file(file_path, use_folder_date, verbose):
    (file_valid, _, file_error_message) = check_file_extension(file_path)
    if file_valid:
        (file_valid, file_error_message) = check_file_location(file_path, use_folder_date)
        if file_valid:
            (file_valid, _, file_error_message) = check_file_name(file_path)
            if file_valid:
                (file_valid, _, file_error_message) = check_file_date(file_path, use_folder_date)
    return (file_valid, file_error_message)

def check_files_in_folder(folder_path, use_folder_date, verbose):
    files_valid = True
    files_error_messages = {}
    file_names = [f for f in os.listdir(folder_path) if os.path.isfile(os.path.join(folder_path, f))]
    if len(file_names):
        for file_name in file_names:
            file_path = os.path.join(folder_path, file_name)
            (file_valid, file_error_message) = check_file(file_path, use_folder_date, verbose)
            files_valid &= file_valid
            file_messages.add_file_message(files_error_messages, folder_path, file_name, file_error_message)
    return (files_valid, files_error_messages)

def process_file(file_path, use_folder_date, set_dates, move_files, rename_files, process, verbose):
    if audio_file.is_file(file_path):
        process_file = audio_file.process_file
    elif picture_file.is_file(file_path):
        process_file = picture_file.process_file
    elif video_file.is_file(file_path):
        process_file = video_file.process_file
    return process_file(file_path, use_folder_date, set_dates, move_files, rename_files, process, verbose)

def check_and_process_files_in_folder(folder_path, use_folder_date, set_dates, move_files, rename_files, process, verbose):
    folder_valid = True
    folder_check_errors = {}
    folder_process_comments = {}

    (folder_date_valid, _) = multimedia_file.has_valid_dated_folder_name(os.path.join(folder_path, ".tmp"))
    if not folder_date_valid:
        (folder_date_valid, _, _) = multimedia_file.has_valid_period_dated_folder_name(os.path.join(folder_path, ".tmp"))

    if folder_date_valid:
        (files_valid, _) = check_files_in_folder(folder_path, use_folder_date, verbose)
        if not files_valid:
            file_names = [f for f in os.listdir(folder_path) if os.path.isfile(os.path.join(folder_path, f))]
            if len(file_names):
                for file_name in file_names:
                    file_path = os.path.join(folder_path, file_name)
                    (file_valid, file_error_message) = check_file(file_path, use_folder_date, verbose)
                    if not file_valid:
                        (file_processed, file_process_comments) = process_file(file_path, use_folder_date, set_dates, move_files, rename_files, process, verbose)
                        if file_processed:
                            file_messages.add_file_messages(folder_process_comments, file_process_comments)
                            file_messages.print_file_messages(folder_process_comments)
                            (file_valid, file_error_message) = check_file(file_path, use_folder_date, verbose)
                        folder_valid &= file_valid
                        if not file_valid:
                            file_messages.add_file_message(folder_check_errors, folder_path, file_name, file_error_message)
                            file_messages.print_file_messages(folder_check_errors)
                        else:                            
                            if verbose:
                                file_messages.add_file_message(folder_check_errors, folder_path, file_name, "Checked")
                                file_messages.print_file_messages(folder_check_errors)
                    else:
                        if verbose:
                            file_messages.add_file_message(folder_check_errors, folder_path, file_name, "Checked")
                            file_messages.print_file_messages(folder_check_errors)
        else:
            if verbose:
                file_messages.add_file_message(folder_check_errors, folder_path, "", "Checked")
                file_messages.print_file_messages(folder_check_errors)
    else:
        folder_valid = False
        file_messages.add_file_message(folder_check_errors, folder_path, "", "Folder date undefined")
        file_messages.print_file_messages(folder_check_errors)

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

    # Generate a flat list of all folders to process
    for folder_path in folder_pathes:
        sub_folder_pathes = get_sub_folders_with_files(folder_path)
        for sub_folder_path in sub_folder_pathes:
            (folder_valid, _, _) = check_and_process_files_in_folder(sub_folder_path, use_folder_date, set_dates, move_files, rename_files, process, verbose)
            valid &= folder_valid

    return (valid, check_errors, process_comments)

def main(argv):
    parser = argparse.ArgumentParser()
    parser.add_argument('-d', '--directory', nargs='+', help="Directory", required=True)
    parser.add_argument('-s', '--set-dates', help="Set dates of files", default=False, action="store_true")
    parser.add_argument('-u', '--use-folder-date', help="Use folder date if no date found for the file", default=False, action="store_true")
    parser.add_argument('-m', '--move-files', help="Move files", default=False, action="store_true")
    parser.add_argument('-r', '--rename-files', help="Rename files", default=False, action="store_true")
    parser.add_argument('-p', '--process', help="Process", default=False, action="store_true")
    parser.add_argument('-v', '--verbose', help="Verbose", default=False, action="store_true")
    args = parser.parse_args(argv)

    (valid, _, _) = check_and_process_files_in_folders(args.directory, args.use_folder_date, args.set_dates, args.move_files, args.rename_files, args.process, args.verbose)
    
    return valid

if __name__ == "__main__":
    main(sys.argv[1:])