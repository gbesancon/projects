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

EXTENSION_PREFIX = {}
for d in [audio_file.AUDIO_EXTENSION_PREFIX, picture_file.PICTURE_EXTENSION_PREFIX, video_file.VIDEO_EXTENSION_PREFIX]:
    EXTENSION_PREFIX.update(d)

def is_year_folder_name(folder_name):
    valid = True
    match = re.match(r"^\d\d\d\d$", folder_name)
    if match:
        valid = True
    else:
        valid = False
    return valid

def is_valid_dated_folder_name(folder_name):
    valid = True
    date = None
    match = re.match(r"^(\d\d\d\d-\d\d-\d\d)[a-z]?\s-\s.*$", folder_name)
    if match:
        valid = True
        date = datetime.datetime.strptime(match.group(1), r'%Y-%m-%d')
    else:
        valid = False
    return (valid, date)

def has_valid_dated_folder_name(file_path):
    valid = False
    folder_name = ""
    while not valid and not is_year_folder_name(folder_name):
        folder_path = file.get_folder_path(file_path)
        folder_name = file.get_folder_name(file_path)
        (valid, date) = is_valid_dated_folder_name(folder_name)
        file_path = folder_path
    return (valid, date)

def get_date_from_folder_name(file_path):
    (_, date) = has_valid_dated_folder_name(file_path)
    return date

def is_valid_period_dated_folder_name(folder_name):
    valid = True
    beginning_date = None
    end_date = None
    match = re.match(r"^(\d\d\d\d-\d\d-\d\d)[a-z]?_(\d\d\d\d-\d\d-\d\d)[a-z]?\s-\s.*$", folder_name)
    if match:
        valid = True
        beginning_date = datetime.datetime.strptime(match.group(1), r'%Y-%m-%d')
        end_date = datetime.datetime.strptime(match.group(2), r'%Y-%m-%d') + datetime.timedelta(days=1) - datetime.timedelta(microseconds=1)
    else:
        valid = False
    return (valid, beginning_date, end_date)

def has_valid_period_dated_folder_name(file_path):
    valid = False
    folder_name = ""
    while not valid and not is_year_folder_name(folder_name):
        folder_path = file.get_folder_path(file_path)
        folder_name = file.get_folder_name(file_path)
        (valid, date) = is_valid_dated_folder_name(folder_name)
        if valid:
            beginning_date = date
            end_date = date + datetime.timedelta(days=1) - datetime.timedelta(microseconds=1)
        else:
            (valid, beginning_date, end_date) = is_valid_period_dated_folder_name(folder_name)
        file_path = folder_path
    return (valid, beginning_date, end_date)

def get_multimedia_file_date(file_path, get_file_date):    
    file_date_valid = False
    file_date = None
    if get_file_date:
        (file_date_valid, file_date) = get_file_date(file_path)
    if not file_date_valid:
        (folder_date_valid, folder_date) = has_valid_dated_folder_name(file_path)
        if folder_date_valid:
            file_date_valid = True
            file_date = folder_date
        else:
            file_date = None
    return (file_date_valid, file_date)

def validate_file_location(file_date, file_path):
    file_location_valid = False
    (folder_date_valid, folder_date) = has_valid_dated_folder_name(file_path)
    if folder_date_valid:
        if file_date:
            if file_date.year == folder_date.year and file_date.month == folder_date.month and file_date.day == folder_date.day:
                file_location_valid = True
    else:
        (folder_period_date_valid, folder_period_beginning_date, folder_period_end_date) = has_valid_period_dated_folder_name(file_path)
        if folder_period_date_valid:
            if file_date:
                if file_date >= folder_period_beginning_date and file_date <= folder_period_end_date:
                    file_location_valid = True
    return file_location_valid

def isclose(a, b, rel_tol=1e-09, abs_tol=0.0):
    return abs(a-b) <= max(rel_tol * max(abs(a), abs(b)), abs_tol)

def check_file_dates(file_path, label_date1, get_date1, label_date2, get_date2, rel_tol=1e-09, abs_tol=0.0):
    valid = False
    date = None
    error_message = None
    date1 = get_date1(file_path)
    date2 = get_date2(file_path)
    if date1:
        if date2:
            if date1.year == date2.year and date1.month == date2.month and date1.day == date2.day:
                if isclose(date1.timestamp(), date2.timestamp(), rel_tol, abs_tol):
                    valid = True
                    date = date1
                else:
                    date = date1
                    error_message = label_date1 + " (" + str(date1) + ")" + " and " +  label_date2 + " (" + str(date2) + ")" + " not matching."
            else:
                date = date1
                error_message = label_date1 + " (" + str(date1) + ")" + " and " +  label_date2 + " (" + str(date2) + ")" + " not matching."
        else:
            date = date1
            error_message = label_date2 + " is undefined."
    else:
        error_message = label_date1 + " is undefined."
    return (valid, date, error_message)

def check_file_date(file_path):
    file_date_valid = False
    file_date = None
    file_error_message = None
    if audio_file.is_audio_file(file_path):
        (file_date_valid, file_date, file_error_message) = check_audio_file_date(file_path)
    elif picture_file.is_picture_file(file_path):
        (file_date_valid, file_date, file_error_message) = check_picture_file_date(file_path)
    elif video_file.is_video_file(file_path):
        (file_date_valid, file_date, file_error_message) = check_video_file_date(file_path)
    return (file_date_valid, file_date, file_error_message)

def check_multimedia_file_date(file_path, file_date):
    file_date_valid = True
    file_error_message = None
    if file_date_valid:
        (file_date_valid, file_date, file_error_message) = check_file_dates(file_path, "File date", lambda f : file_date, "Folder date", get_date_from_folder_name, 24*60*60)
    if file_date_valid:
        (file_date_valid, file_date, file_error_message) = check_file_dates(file_path, "File date", lambda f : file_date, "Creation date", file.get_creation_date)
    if file_date_valid:
        (file_date_valid, file_date, file_error_message) = check_file_dates(file_path, "File date", lambda f : file_date, "Modification date", file.get_modification_date)
    return (file_date_valid, file_date, file_error_message)

def check_picture_file_date(file_path):
    (file_date_valid, file_date) = get_multimedia_file_date(file_path, picture_file.get_picture_file_date)
    file_error_message = None
    if file_date_valid:
        if picture_file.has_exif_date_time_original(file_path):
            if file_date_valid:
                (file_date_valid, file_date, file_error_message) = check_file_dates(file_path, "File date", lambda f : file_date, "EXIF Date Time Original", picture_file.get_exif_date_time_original)
        if picture_file.has_exif_date_time_digitized(file_path):
            if file_date_valid:
                (file_date_valid, file_date, file_error_message) = check_file_dates(file_path, "File date", lambda f : file_date, "EXIF Date Time Digitized", picture_file.get_exif_date_time_digitized)
        if file_date_valid:
            (file_date_valid, file_date, file_error_message) = check_multimedia_file_date(file_path, file_date)
    else:
        file_error_message = "No date identified for file"
    return (file_date_valid, file_date, file_error_message)

def check_audio_file_date(file_path):
    (_, file_date) = get_multimedia_file_date(file_path, None)
    return check_multimedia_file_date(file_path, file_date)

def check_video_file_date(file_path):
    (_, file_date) = get_multimedia_file_date(file_path, None)
    return check_multimedia_file_date(file_path, file_date)

def has_valid_extension(file_path):
    file_extension = file.get_file_extension(file_path)
    return file_extension in EXTENSION_PREFIX

def is_valid_file_name(file_path):
    valid = True
    file_name = file.get_file_name(file_path)
    file_name_no_extension = os.path.splitext(file_name)[0]
    file_extension = file.get_file_extension(file_path)
    prefix = EXTENSION_PREFIX[file_extension]
    match = re.match(r"^" + prefix + r"\d\d\d\d\d$", file_name_no_extension)
    if match:
        valid = True
    else:
        valid = False
    return valid

def check_file_name(file_path):
    valid = False
    error_message = None
    folder_name = file.get_folder_name(file_path)
    if has_valid_extension(file_path):
        if is_valid_file_name(file_path):
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

def set_audio_file_dates(folder_path, audio_file_names, process, verbose):
    files_processed = False
    files_process_comments = {}

    for audio_file_name in audio_file_names:
        audio_file_path = os.path.join(folder_path, audio_file_name)
        (file_date_valid, file_date) = get_multimedia_file_date(audio_file_path, None)
        (folder_date_valid, folder_date) = has_valid_dated_folder_name(audio_file_path)
        if file_date_valid:
            if folder_date_valid:
                if file_date.year == folder_date.year and file_date.month == folder_date.month and file_date.day == folder_date.day:
                    if process:
                        audio_file.set_audio_file_date(audio_file_path, file_date)
                    if not process or verbose:
                        file_messages.add_file_message(files_process_comments, audio_file_path, "Set File date (" + str(file_date) + ")")
                else:
                    file_messages.add_file_message(files_process_comments, audio_file_path, "File date (" + str(file_date) + ") not matching dated folder (" + folder_path + ")")
            else:
                pass
        else:
            if folder_date_valid:
                if process:
                    audio_file.set_audio_file_date(audio_file_path, folder_date)
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

def process_audio_files_in_folder(folder_path, audio_file_names, process, verbose):
    files_processed = False
    files_process_comments = {}

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

def set_picture_file_dates(folder_path, picture_file_names, process, verbose):
    files_processed = False
    files_process_comments = {}

    for picture_file_name in picture_file_names:
        picture_file_path = os.path.join(folder_path, picture_file_name)
        (file_date_valid, file_date) = get_multimedia_file_date(picture_file_path, picture_file.get_picture_file_date)
        if file_date_valid:
            (folder_date_valid, folder_date) = has_valid_dated_folder_name(picture_file_path)
            if folder_date_valid:
                if file_date.year == folder_date.year and file_date.month == folder_date.month and file_date.day == folder_date.day:
                    if process:
                        picture_file.set_picture_file_date(picture_file_path, file_date)
                    if not process or verbose:
                        file_messages.add_file_message(files_process_comments, picture_file_path, "Set File date (" + str(file_date) + ")")
                else:
                    file_messages.add_file_message(files_process_comments, picture_file_path, "File date (" + str(file_date) + ") not matching dated folder (" + folder_path + ")")
            else:
                (folder_period_date_valid, folder_period_beginning_date, folder_period_end_date) = has_valid_period_dated_folder_name(picture_file_path)
                if folder_period_date_valid:
                    if folder_period_beginning_date <= file_date and file_date <= folder_period_end_date:
                        if process:
                            picture_file.set_picture_file_date(picture_file_path, file_date)
                        if not process or verbose:
                            file_messages.add_file_message(files_process_comments, picture_file_path, "Set File date (" + str(file_date) + ")")
                    else:
                        file_messages.add_file_message(files_process_comments, picture_file_path, "File date (" + str(file_date) + ") not matching dated folder (" + folder_path + ")")
        else:
            file_messages.add_file_message(files_process_comments, picture_file_path, "No date identified for file")

    files_processed = process
    return (files_processed, files_process_comments)

def move_picture_files(folder_path, picture_file_names, process, verbose):
    files_processed = False
    files_process_comments = {}
    regular_file_names = [f for f in picture_file_names if is_valid_file_name(os.path.join(folder_path, f))]
    panorama_file_names = [f for f in picture_file_names if picture_file.is_valid_panorama_file_name(os.path.join(folder_path, f))]
    other_file_names = [f for f in picture_file_names if f not in regular_file_names and f not in panorama_file_names]
    if not picture_file.is_panorama_folder_name(os.path.basename(folder_path)):
        if len(panorama_file_names) > 0:
            # Move panorama files to Panorama folder
            panorama_folder_path = os.path.join(folder_path, picture_file.PANORAMA_FOLDER_NAME)
            for panorama_file_name in panorama_file_names:
                panorama_file_path = os.path.join(folder_path, panorama_file_name)
                if process:
                    file.move_file_to_folder(panorama_file_path, panorama_folder_path)
                if not process or verbose:
                    file_messages.add_file_message(files_process_comments, panorama_file_path, "Move to " + panorama_folder_path)
    else:
        if len(panorama_file_names) > 0:
            # Move regular files and other files to parent folder
            parent_folder_path = file.get_folder_path(folder_path)
            for panorama_file_name in (regular_file_names + other_file_names):
                panorama_file_path = os.path.join(folder_path, panorama_file_name)
                if process:
                    file.move_file_to_folder(panorama_file_path, parent_folder_path)
                if not process or verbose:
                    file_messages.add_file_message(files_process_comments, panorama_file_path, "Move to " + parent_folder_path)
    
    # Create daily folders
    for picture_file_name in (regular_file_names + other_file_names):
        picture_file_path = os.path.join(folder_path, picture_file_name)
        (file_date_valid, file_date) = get_multimedia_file_date(picture_file_path, picture_file.get_picture_file_date)
        if file_date_valid:
            (folder_date_valid, folder_date) = has_valid_dated_folder_name(picture_file_path)
            if folder_date_valid:
                if not (file_date.year == folder_date.year and file_date.month == folder_date.month and file_date.day == folder_date.day):
                    dated_folder_name = "{:0>4d}-{:0>2d}-{:0>2d} - XXX".format(file_date.year, file_date.month, file_date.day)
                    dated_folder_path = os.path.join(folder_path, dated_folder_name)
                    if process:
                        file.move_file_to_folder(picture_file_path, dated_folder_path)
                    if not process or verbose:
                        file_messages.add_file_message(files_process_comments, picture_file_path, "Move to " + dated_folder_path)
            else:
                (folder_period_date_valid, folder_period_beginning_date, folder_period_end_date) = has_valid_period_dated_folder_name(picture_file_path)
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

def rename_picture_files(folder_path, file_names, process, verbose):
    files_processed = False
    files_process_comments = {}
    
    files_processed = process
    return (files_processed, files_process_comments)

def process_picture_files_in_folder(folder_path, picture_file_names, process, verbose):
    files_processed = False
    files_process_comments = {}

    # Change file dates
    (picture_file_dates_processed, picture_file_dates_process_comments) = set_picture_file_dates(folder_path, picture_file_names, process, verbose)
    files_processed &= picture_file_dates_processed
    file_messages.add_file_messages(files_process_comments, picture_file_dates_process_comments)
    # Move files
    (move_picture_files_processed, move_picture_files_process_comments) = move_picture_files(folder_path, picture_file_names, process, verbose)
    files_processed &= move_picture_files_processed
    file_messages.add_file_messages(files_process_comments, move_picture_files_process_comments)
    # Rename files
    (rename_picture_files_processed, rename_picture_files_process_comments) = rename_picture_files(folder_path, picture_file_names, process, verbose)
    files_processed &= rename_picture_files_processed
    file_messages.add_file_messages(files_process_comments, rename_picture_files_process_comments)
    
    files_processed = process
    return (files_processed, files_process_comments)

def set_video_file_dates(folder_path, video_file_names, process, verbose):
    files_processed = False
    files_process_comments = {}

    for video_file_name in video_file_names:
        video_file_path = os.path.join(folder_path, video_file_name)
        (file_date_valid, file_date) = get_multimedia_file_date(video_file_path, None)
        (folder_date_valid, folder_date) = has_valid_dated_folder_name(video_file_path)
        if file_date_valid:
            if folder_date_valid:
                if file_date.year == folder_date.year and file_date.month == folder_date.month and file_date.day == folder_date.day:
                    if process:
                        video_file.set_video_file_date(video_file_path, file_date)
                    if not process or verbose:
                        file_messages.add_file_message(files_process_comments, video_file_path, "Set File date (" + str(file_date) + ")")
                else:
                    file_messages.add_file_message(files_process_comments, video_file_path, "File date (" + str(file_date) + ") not matching dated folder (" + folder_path + ")")
            else:
                pass
        else:
            if folder_date_valid:
                if process:
                    video_file.set_video_file_date(video_file_path, folder_date)
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

def process_video_files_in_folder(folder_path, video_file_names, process, verbose):
    files_processed = False
    files_process_comments = {}
    
    # Change file dates
    (video_file_dates_processed, video_file_dates_process_comments) = set_video_file_dates(folder_path, video_file_names, process, verbose)
    files_processed &= video_file_dates_processed
    file_messages.add_file_messages(files_process_comments, video_file_dates_process_comments)
    # Move files
    (move_video_files_processed, move_video_files_process_comments) = move_video_files(folder_path, video_file_names, process, verbose)
    files_processed &= move_video_files_processed
    file_messages.add_file_messages(files_process_comments, move_video_files_process_comments)
    # Rename files
    (rename_video_files_processed, rename_video_files_process_comments) = rename_video_files(folder_path, video_file_names, process, verbose)
    files_processed &= rename_video_files_processed
    file_messages.add_file_messages(files_process_comments, rename_video_files_process_comments)
    
    files_processed = process
    return (files_processed, files_process_comments)

def process_files_in_folder(folder_path, file_names, process, verbose):
    files_processed = False
    files_process_comments = {}

    audio_file_names = [f for f in file_names if file.get_file_extension(os.path.join(folder_path, f)) in audio_file.AUDIO_EXTENSION_PREFIX]
    if len(audio_file_names) > 0:
        (audio_files_processed, audio_files_process_comments) = process_audio_files_in_folder(folder_path, audio_file_names, process, verbose)
        files_processed &= audio_files_processed
        file_messages.add_file_messages(files_process_comments, audio_files_process_comments)

    picture_file_names = [f for f in file_names if file.get_file_extension(os.path.join(folder_path, f)) in picture_file.PICTURE_EXTENSION_PREFIX]
    if len(picture_file_names) > 0:
        (picture_files_processed, picture_files_process_comments) = process_picture_files_in_folder(folder_path, picture_file_names, process, verbose)
        files_processed &= picture_files_processed
        file_messages.add_file_messages(files_process_comments, picture_files_process_comments)
    
    video_file_names = [f for f in file_names if file.get_file_extension(os.path.join(folder_path, f)) in video_file.VIDEO_EXTENSION_PREFIX]
    if len(video_file_names) > 0:
        (video_files_processed, video_files_process_comments) = process_video_files_in_folder(folder_path, video_file_names, process, verbose)
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