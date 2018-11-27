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
import folder

def check_and_process_files_in_folder(folder_path, use_folder_date, set_dates, move_files, rename_files, process, verbose):
    folder_valid = True
    folder_check_errors = {}
    folder_process_comments = {}
    _folder = folder.Folder(folder_path)
    if _folder.has_valid_dated_folder_name():
        files_valid = _folder.check_files(use_folder_date, verbose)
        if not files_valid:
            file_names = [f for f in os.listdir(folder_path) if os.path.isfile(os.path.join(folder_path, f))]
            if len(file_names):
                print(folder_path + ":")
                for file_name in file_names:
                    file_path = os.path.join(folder_path, file_name)
                    _file = _folder._get_file(file_path)
                    if _file:
                        if _file.exists():
                            (file_valid, file_error_message) = _file.check_file(use_folder_date, verbose)
                            if not file_valid:
                                (file_processed, file_process_comments) = _file.process_file(use_folder_date, set_dates, move_files, rename_files, process, verbose)
                                if not file_processed or verbose:
                                    for file_process_comment in file_process_comments:
                                        file_messages.add_file_message(folder_process_comments, folder_path, file_name, file_process_comment)
                                        print(" - " + file_name + ":", file_process_comment)
                                if file_processed:
                                    (file_valid, file_error_message) = _file.check_file(use_folder_date, verbose)
                                folder_valid &= file_valid
                                if not file_valid:
                                    file_messages.add_file_message(folder_check_errors, _file.get_folder().get_folder_path(), _file.get_file_name(), file_error_message)
                                    print(" - " + _file.get_file_name() + ":", file_error_message)
                                else:                            
                                    if verbose:
                                        file_error_message = "Checked"
                                        file_messages.add_file_message(folder_check_errors, _file.get_folder().get_folder_path(), _file.get_file_name(), file_error_message)
                                        print(" - " + _file.get_file_name() + ":", file_error_message)
                            else:
                                if verbose:
                                    file_error_message = "Checked"
                                    file_messages.add_file_message(folder_check_errors, _file.get_folder().get_folder_path(), _file.get_file_name(), file_error_message)
                                    print(" - " + _file.get_file_name() + ":", file_error_message)
        else:
            if verbose:
                folder_error_message = "Checked"
                file_messages.add_file_message(folder_check_errors, folder_path, "", folder_error_message)
                print(folder_path + ": " + folder_error_message)
    else:
        folder_valid = False
        folder_error_message = "Folder date undefined"
        file_messages.add_file_message(folder_check_errors, folder_path, "", folder_error_message)
        print(folder_path + ": " + folder_error_message)
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