from __future__ import print_function
import argparse
import os
import platform
import datetime
import sys
import re
import piexif

AUDIO_PREFIX = "a"
AUDIO_EXTENSION_PREFIX = {    
    ".mp3" : AUDIO_PREFIX,
    ".wav" : AUDIO_PREFIX,
    ".ogg" : AUDIO_PREFIX,
}

PICTURE_PREFIX = "p"
EXIF_PICTURE_EXTENSION_PREFIX = {
    ".jpg" : PICTURE_PREFIX,
    ".jpeg": PICTURE_PREFIX,
    ".png" : PICTURE_PREFIX,
    ".tif" : PICTURE_PREFIX,
    ".tiff": PICTURE_PREFIX,
}
NON_EXIF_PICTURE_EXTENSION_PREFIX = {    
    ".bmp" : PICTURE_PREFIX,
    ".gif" : PICTURE_PREFIX,
    ".orf" : PICTURE_PREFIX,
}
PICTURE_EXTENSION_PREFIX = {}
for d in [EXIF_PICTURE_EXTENSION_PREFIX, NON_EXIF_PICTURE_EXTENSION_PREFIX]:
    PICTURE_EXTENSION_PREFIX.update(d)

VIDEO_PREFIX = "v"
VIDEO_EXTENSION_PREFIX = {    
    ".avi" : VIDEO_PREFIX,
    ".mp4" : VIDEO_PREFIX,
    ".mov" : VIDEO_PREFIX,
    ".mpg" : VIDEO_PREFIX,
    ".mpeg": VIDEO_PREFIX,
    ".mts" : VIDEO_PREFIX,
    ".wmv" : VIDEO_PREFIX,
    ".3gp" : VIDEO_PREFIX,
    ".flv" : VIDEO_PREFIX,
    ".ogv" : VIDEO_PREFIX,
}

EXTENSION_PREFIX = {}
for d in [AUDIO_EXTENSION_PREFIX, PICTURE_EXTENSION_PREFIX, VIDEO_EXTENSION_PREFIX]:
    EXTENSION_PREFIX.update(d)

PANORAMA_FOLDER_NAME = "Panorama"

def is_panorama_folder(folder_name):
    return folder_name == PANORAMA_FOLDER_NAME

def print_file_process_comment(file_path, process_comment):
    print(file_path + ": " + process_comment)

OK_MESSAGE = "OK"

def print_file_error_message(file_path, error_message):
    if not error_message:
        error_message = OK_MESSAGE
    print(file_path + ": " + error_message)

def get_folder_path(file_path):
    return os.path.dirname(file_path)

def get_folder_name(file_path):    
    folder_path = get_folder_path(file_path)
    return os.path.basename(folder_path)

def get_file_name(file_path):
    return os.path.basename(file_path)

def get_file_extension(file_path):
    extensions = os.path.splitext(get_file_name(file_path))
    return extensions[len(extensions) - 1].lower()

def is_year_folder_name(folder_name):
    valid = True
    match = re.match("^\d\d\d\d$", folder_name)
    if match:
        valid = True
    else:
        valid = False
    return valid

def is_valid_dated_folder_name(folder_name):
    valid = True
    date = None
    match = re.match("^(\d\d\d\d-\d\d-\d\d)[a-z]?.*$", folder_name)
    if match:
        valid = True
        date = datetime.datetime.strptime(match.group(1), '%Y-%m-%d')
    else:
        valid = False
    return (valid, date)

def has_valid_dated_folder_name(file_path):
    valid = False
    folder_name = ""
    while not valid and not is_year_folder_name(folder_name):
        folder_path = get_folder_path(file_path)
        folder_name = get_folder_name(file_path)
        (valid, date) = is_valid_dated_folder_name(folder_name)
        file_path = folder_path
    return (valid, date)

def get_date_from_folder_name(file_path):
    (_, date) = has_valid_dated_folder_name(file_path)
    return date

def is_valid_dated_file_name(file_path):
    valid = True
    date = None
    file_name = get_file_name(file_path)
    match = re.match("^IMG_(\d\d\d\d\d\d\d\d_\d\d\d\d\d\d).*$", file_name)
    if match:
        valid = True
        date = datetime.datetime.strptime(match.group(1), '%Y%m%d_%H%M%S')
    else:
        valid = False
    return (valid, date)

def get_date_from_file_name(file_path):
    (_, date) = is_valid_dated_file_name(file_path)
    return date

def get_date_from_file_or_folder(file_path):
    (valid, date) = is_valid_dated_file_name(file_path)
    if not valid:
        date = get_date_from_folder_name(file_path)           
    return date
    
def has_exif(file_path):
    has = False
    if get_file_extension(file_path) in EXIF_PICTURE_EXTENSION_PREFIX:
        has = get_exif(file_path) is not None
    return has

def get_exif(file_path):
    exif_dict = None
    try:
        exif_dict = piexif.load(file_path)
    except:
        pass
    return exif_dict

def has_exif_date_time_original(file_path):
    return get_exif_date_time_original(file_path) is not None

def get_exif_date_time_original(file_path):
    exif_date_time_original = None
    if has_exif(file_path):
        exif_dict = get_exif(file_path)
        if "Exif" in exif_dict:
            exif = exif_dict["Exif"]
            if exif:
                if piexif.ExifIFD.DateTimeOriginal in exif:
                    date_time_original_binary = exif[piexif.ExifIFD.DateTimeOriginal]
                    if date_time_original_binary:
                        date_time_original = date_time_original_binary.decode("utf-8")
                        exif_date_time_original = datetime.datetime.strptime(date_time_original, '%Y:%m:%d %H:%M:%S')
    return exif_date_time_original

def has_exif_date_time_digitized(file_path):
    return get_exif_date_time_digitized(file_path) is not None

def get_exif_date_time_digitized(file_path):
    exif_date_time_digitized = None
    if has_exif(file_path):
        exif_dict = get_exif(file_path)
        if "Exif" in exif_dict:
            exif = exif_dict["Exif"]
            if exif:
                if piexif.ExifIFD.DateTimeDigitized in exif:
                    date_time_digitized_binary = exif[piexif.ExifIFD.DateTimeDigitized]
                    if date_time_digitized_binary:
                        date_time_digitized = date_time_digitized_binary.decode("utf-8")
                        exif_date_time_digitized = datetime.datetime.strptime(date_time_digitized, '%Y:%m:%d %H:%M:%S')
    return exif_date_time_digitized

def get_creation_date(file_path):
    creation_date = None
    """
    Try to get the date that a file was created, falling back to when it was
    last modified if that isn't possible.
    See http://stackoverflow.com/a/39501288/1709587 for explanation.
    """
    if platform.system() == 'Windows':
        creation_date = os.path.getctime(file_path)
    else:
        stat = os.stat(file_path)
        try:
            creation_date = stat.st_birthtime
        except AttributeError:
            # We're probably on Linux. No easy way to get creation dates here,
            # so we'll settle for when its content was last modified.
            creation_date = stat.st_mtime
    return datetime.datetime.fromtimestamp(creation_date)

def get_modification_date(file_path):
    modification_date = os.path.getmtime(file_path) 
    return datetime.datetime.fromtimestamp(modification_date)        

def get_file_date(file_path):
    file_date = None
    if has_exif_date_time_original(file_path):
        file_date = get_exif_date_time_original(file_path)
    elif has_exif_date_time_digitized(file_path):
        file_date = get_exif_date_time_digitized(file_path)
    else:
        (valid, file_date) = is_valid_dated_file_name(file_path)
        if not valid:
            file_date = None
    return file_date

def set_file_date(file_path, file_date):
    pass

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
    file_valid = False
    file_date = get_file_date(file_path)
    file_error_message = None
    if file_date:
        file_valid = True
        (file_valid, file_date, file_error_message) = check_file_dates(file_path, "File date", lambda f : file_date, "EXIF Date Time Original", get_exif_date_time_original)
        if file_valid:
            (file_valid, file_date, file_error_message) = check_file_dates(file_path, "File date", lambda f : file_date, "EXIF Date Time Digitized", get_exif_date_time_digitized)
            if file_valid:
                (file_valid, file_date, file_error_message) = check_file_dates(file_path, "File date", lambda f : file_date, "Folder date", get_date_from_file_or_folder, 24*60*60)
                if file_valid:
                    (file_valid, file_date, file_error_message) = check_file_dates(file_path, "File date", lambda f : file_date, "Creation date", get_creation_date)
                    if file_valid:
                        (file_valid, file_date, file_error_message) = check_file_dates(file_path, "File date", lambda f : file_date, "Modification date", get_modification_date)
    else:
        file_error_message = "No date identified for file"
    return (file_valid, file_date, file_error_message)

def has_valid_extension(file_path):
    file_extension = get_file_extension(file_path)
    return file_extension in EXTENSION_PREFIX

def is_valid_file_name(file_path):
    valid = True
    file_name = get_file_name(file_path)
    file_name_no_extension = os.path.splitext(file_name)[0]
    file_extension = get_file_extension(file_path)
    prefix = EXTENSION_PREFIX[file_extension]
    match = re.match("^" + prefix + "\d\d\d\d\d$", file_name_no_extension)
    if match:
        valid = True
    else:
        valid = False
    return valid

def is_valid_panorama_file_name(file_path):
    valid = True
    file_name = get_file_name(file_path)
    file_name_no_extension = os.path.splitext(file_name)[0]
    file_extension = get_file_extension(file_path)
    prefix = EXTENSION_PREFIX[file_extension]
    match = re.match("^" + prefix + "\d\d\d\d\d" + " - " + prefix + "\d\d\d\d\d" + "$", file_name_no_extension)
    if match:
        valid = True
    else:
        valid = False
    return valid

def check_file_name(file_path):
    valid = False
    error_message = None
    folder_name = get_folder_name(file_path)
    if has_valid_extension(file_path):
        if is_valid_file_name(file_path):
            if not is_panorama_folder(folder_name):
                valid = True
            else:
                valid = False
                error_message = "File should not be stored in a Panorama folder"
        else:
            if is_valid_panorama_file_name(file_path):
                if is_panorama_folder(folder_name):
                    valid = True
                else:
                    valid = False
                    error_message = "Panorama file not stored in a Panorama folder"
            else:
                if is_panorama_folder(folder_name):
                    valid = False
                    error_message = "Panorama filename incorrect"
                else:
                    valid = False
                    error_message = "Filename incorrect"
    else:
        valid = False
        error_message = "Extension " + get_file_extension(file_path) + " not supported"
    return (valid, error_message)

def check_file(file_path, verbose):
    (file_valid, file_error_message) = check_file_name(file_path)
    if file_valid:
        (file_valid, _, file_error_message) = check_file_date(file_path)
    return (file_valid, file_error_message)

def check_and_process_sub_folders_in_folder(folder_path, sub_folder_names, process, verbose):
    sub_folders_valid = True
    sub_folders_check_errors = []
    sub_folders_process_comments = []
    for sub_folder_name in sub_folder_names:
        sub_folder_path = os.path.join(folder_path, sub_folder_name)
        (sub_folder_valid, sub_folder_check_errors, sub_folder_process_comments) = check_and_process_files_and_sub_folders_in_folder(sub_folder_path, process, verbose)
        sub_folders_valid &= sub_folder_valid
        sub_folders_check_errors += sub_folder_check_errors
        sub_folders_process_comments += sub_folder_process_comments
    return (sub_folders_valid, sub_folders_check_errors, sub_folders_process_comments)

def check_files_in_folder(folder_path, file_names, verbose):
    files_valid = True
    files_errors = []
    for file_name in file_names:
        file_path = os.path.join(folder_path, file_name)
        (file_valid, file_error_message) = check_file(file_path, verbose)
        files_valid &= file_valid
        if not file_valid or verbose:
            files_errors += [(file_path, file_error_message)]
    return (files_valid, files_errors)

def process_files_in_folder(folder_path, file_names, process, verbose):
    files_processed = False
    files_process_comments = []

    # Change file dates
    for file_name in file_names:
        file_path = os.path.join(folder_path, file_name)
        file_date = get_file_date(file_path)
        if file_date:
            (valid, folder_date) = has_valid_dated_folder_name(file_path)
            if valid:
                if file_date.year == folder_date.year and file_date.month == folder_date.month and file_date.day == folder_date.day:
                    set_file_date(file_path, file_date)
                    files_process_comments += [(file_path, "Set File date (" + str(file_date) + ")")]
                else:
                    files_process_comments += [(file_path, "File date (" + str(file_date) + ") not matching dated folder (" + folder_path + ")")]
            else:
                files_process_comments += [(file_path, "File date (" + str(file_date) + ") not stored in a dated folder (" + folder_path + ")")]
        else:
            files_process_comments += [(file_path, "No date identified for file")]

    # Move files
    regular_file_names = [f for f in file_names if is_valid_file_name(os.path.join(folder_path, f))]
    panorama_file_names = [f for f in file_names if is_valid_panorama_file_name(os.path.join(folder_path, f))]
    other_file_names = [f for f in file_names if f not in regular_file_names and f not in panorama_file_names]
    if not is_panorama_folder(os.path.basename(folder_path)):
        if len(panorama_file_names) > 0:
            # Move panorama files to Panorama folder
            panorama_folder_path = os.path.join(folder_path, PANORAMA_FOLDER_NAME)
            files_process_comments += [(folder_path, "Move " + str(panorama_file_names) + " to " + panorama_folder_path)]
    else:
        if len(panorama_file_names) > 0:
            # Move regular files and other files to parent folder
            parent_folder_path = get_folder_path(folder_path)
            files_process_comments += [(folder_path, "Move " + str(regular_file_names + other_file_names) + " to " + parent_folder_path)]
    
    # Create daily folders
    for file_name in (regular_file_names + other_file_names):
        file_path = os.path.join(folder_path, file_name)
        file_date = get_file_date(file_path)
        if file_date:
            (valid, folder_date) = has_valid_dated_folder_name(file_path)
            if valid:
                if file_date.year == folder_date.year and file_date.month == folder_date.month and file_date.day == folder_date.day:
                    pass
                else:
                    dated_folder_name = "{:0>4d}-{:0>2d}-{:0>2d} - ".format(file_date.year, file_date.month, file_date.day)
                    dated_folder_path = os.path.join(folder_path, dated_folder_name)
                    files_process_comments += [(file_path, "Move to " + dated_folder_path)]
            else:
                dated_folder_name = "{:0>4d}-{:0>2d}-{:0>2d} - ".format(file_date.year, file_date.month, file_date.day)
                dated_folder_path = os.path.join(folder_path, dated_folder_name)
                files_process_comments += [(file_path, "Move to " + dated_folder_path)]
        else:
            files_process_comments += [(file_path, "No date identified for file")]

    # Rename files


    files_processed = process

    if not files_processed:
        for files_process_comment in files_process_comments:
            print_file_process_comment(files_process_comment[0], files_process_comment[1])
    return (files_processed, files_process_comments)

def check_and_process_files_and_sub_folders_in_folder(folder_path, process, verbose):
    folder_valid = True
    folder_check_errors = []
    folder_process_comments = []
    if os.path.exists(folder_path):
        for path, dirs, files in os.walk(folder_path):
            if len(dirs) > 0:
                (sub_folders_valid, sub_folders_check_errors, sub_folders_process_comments) = check_and_process_sub_folders_in_folder(path, dirs, process, verbose)
                folder_valid &= sub_folders_valid
                folder_check_errors += sub_folders_check_errors
                folder_process_comments += sub_folders_process_comments
            if len(files):
                (files_valid, files_check_errors) = check_files_in_folder(path, files, verbose)
                folder_valid &= files_valid
                if not files_valid:
                    (files_processed, files_process_comments) = process_files_in_folder(path, files, process, verbose)
                    folder_valid &= files_processed
                    folder_process_comments += files_process_comments
                    (files_valid, files_check_errors) = check_files_in_folder(path, files, verbose)
                    folder_valid &= files_valid
                    folder_check_errors += files_check_errors
                    if not files_valid :
                        for check_error in files_check_errors:
                            print_file_error_message(check_error[0], check_error[1])
    else:
        folder_valid = False
        folder_check_errors += [ folder_path + " doesn't exist."]
    return (folder_valid, folder_check_errors, folder_process_comments)

def check_and_process_files_in_folders(folder_pathes, process, verbose):
    valid = True
    check_errors = []
    process_comments = []
    for folder_path in folder_pathes:
        (folder_valid, folder_check_errors, folder_process_comments) = check_and_process_files_and_sub_folders_in_folder(folder_path, process, verbose)
        valid &= folder_valid
        check_errors += folder_check_errors
        process_comments += folder_process_comments
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