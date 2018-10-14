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
    match = re.match("^(\d\d\d\d-\d\d-\d\d)[a-z]?\s-\s.*$", folder_name)
    if match:
        valid = True
        date = datetime.datetime.strptime(match.group(1), '%Y-%m-%d')
    else:
        valid = False
    return (valid, date)

def get_date_from_folder_name(file_path):
    valid = False
    folder_name = ""
    while not valid and not is_year_folder_name(folder_name):
        folder_path = get_folder_path(file_path)
        folder_name = get_folder_name(file_path)
        (valid, date) = is_valid_dated_folder_name(folder_name)
        file_path = folder_path
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

def isclose(a, b, rel_tol=1e-09, abs_tol=0.0):
    return abs(a-b) <= max(rel_tol * max(abs(a), abs(b)), abs_tol)

def check_file_dates(file_path, label_date1, get_date1, label_date2, get_date2, rel_tol=1e-09, abs_tol=0.0):
    valid = False
    error_message = None
    date1 = get_date1(file_path)
    date2 = get_date2(file_path)
    if date1:
        if date2:
            if (date1.year == date2.year and date1.month == date2.month and date1.minute == date2.minute) \
                and isclose(date1.timestamp(), date2.timestamp(), rel_tol, abs_tol):
                valid = True
            else:
                valid = False
                error_message = label_date1 + " (" + str(date1) + ")" + " and " +  label_date2 + " (" + str(date2) + ")" + " not matching."
        else:
            valid = False
            error_message = label_date2 + " is undefined."
    else:
        valid = False
        error_message = label_date1 + " is undefined."
    return (valid, error_message)

def check_file_date(file_path):
    (valid, error_message) = check_file_dates(file_path, "Folder date", get_date_from_file_or_folder, "EXIF Date Time Original", get_exif_date_time_original, 24*60*60)
    if valid:
        (valid, error_message) = check_file_dates(file_path, "EXIF Date Time Original", get_exif_date_time_original, "Creation date", get_creation_date)
        if valid:
            (valid, error_message) = check_file_dates(file_path, "EXIF Date Time Original", get_exif_date_time_original, "Modification date", get_modification_date)
    return (valid, error_message)

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

def check_file_name(file_path, verbose):
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
    (file_valid, file_error_message) = check_file_name(file_path, verbose)
    if file_valid:
        (file_valid, file_error_message) = check_file_date(file_path)    
    if verbose or not file_valid:
        print_file_error_message(file_path, file_error_message)
    return (file_valid, file_error_message)

def check_and_process_sub_folders_in_folder(folder_path, sub_folder_names, process, verbose):
    sub_folders_valid = True
    sub_folders_errors = []
    for sub_folder_name in sub_folder_names:
        sub_folder_path = os.path.join(folder_path, sub_folder_name)
        (sub_folder_valid, sub_folder_errors) = check_and_process_files_and_sub_folders_in_folder(sub_folder_path, process, verbose)
        sub_folders_valid &= sub_folder_valid
        sub_folders_errors += sub_folder_errors
    return (sub_folders_valid, sub_folders_errors)

def check_files_in_folder(folder_path, file_names, verbose):
    files_valid = True
    files_errors = []
    for file_name in file_names:
        file_path = os.path.join(folder_path, file_name)
        (file_valid, file_error_message) = check_file(file_path, verbose)
        files_valid &= file_valid
        files_errors += [(file_path, file_error_message)]
    return (files_valid, files_errors)

def process_files_in_folder(folder_path, file_names, process, verbose):
    files_done = True
    files_comments = []
    return (files_done, files_comments)

def check_and_process_files_and_sub_folders_in_folder(folder_path, process, verbose):
    folder_valid = True
    folder_errors = []
    if os.path.exists(folder_path):
        for path, dirs, files in os.walk(folder_path):
            (sub_folders_valid, sub_folders_errors) = check_and_process_sub_folders_in_folder(path, dirs, process, verbose)
            folder_valid &= sub_folders_valid
            folder_errors += sub_folders_errors
            (files_valid, files_errors) = check_files_in_folder(path, files, verbose)
            #folder_valid &= files_valid
            #folder_errors += files_errors
            if not files_valid:
                (files_done, files_comments) = process_files_in_folder(path, files, process, verbose)
                folder_valid &= files_done
                folder_errors += files_comments
                (files_valid2, files_errors2) = check_files_in_folder(path, files, verbose)
                folder_valid &= files_valid2
                folder_errors += files_errors2

    else:
        folder_valid = False
        folder_errors += [ folder_path + " doesn't exist."]
    return (folder_valid, folder_errors)

def check_and_process_files_in_folders(folder_pathes, process, verbose):
    valid = True
    errors = []
    for folder_path in folder_pathes:
        (folder_valid, folder_errors) = check_and_process_files_and_sub_folders_in_folder(folder_path, process, verbose)
        valid &= folder_valid
        errors += folder_errors
    return (valid, errors)

def main(argv):
    parser = argparse.ArgumentParser()
    parser.add_argument('-d', '--directory', nargs='+', help="Directory", required=True)
    parser.add_argument('-p', '--process', help="Rename files and set dates", default=False, action="store_true")
    parser.add_argument('-v', '--verbose', help="Verbose", default=False, action="store_true")
    args = parser.parse_args(argv)
    (valid, errors) = check_and_process_files_in_folders(args.directory, args.process, args.verbose)
    return valid

if __name__ == "__main__":
    main(sys.argv[1:])