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

OK_MESSAGE = "OK"

PANORAMA_FOLDER_NAME = "Panorama"

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

def check_and_process_files_in_folder(folder_path, process, verbose):
    folder_valid = True
    folder_invalid_files = []
    for path,dirs,files in os.walk(folder_path):
        for dir_name in dirs:
            subfolder_path = os.path.join(path,dir_name)
            (subfolder_valid, subfolder_invalid_files) = check_and_process_files_in_folder(subfolder_path, process, verbose)
            folder_valid &= subfolder_valid
            folder_invalid_files += subfolder_invalid_files
        for file_name in files:
            file_path = os.path.join(path,file_name)
            (file_name_valid, file_name_error_message) = check_file_name(file_path, verbose)
            folder_valid &= file_name_valid
            if not file_name_valid:
                folder_invalid_files += [(file_path, file_name_error_message)]
            else:    
                (file_date_valid, file_date_error_message) = check_file_date(file_path)
                folder_valid &= file_date_valid
                if not file_date_valid:
                    folder_invalid_files += [(file_path, file_date_error_message)]

    return (folder_valid, folder_invalid_files)

def isclose(a, b, rel_tol=1e-09, abs_tol=0.0):
    return abs(a-b) <= max(rel_tol * max(abs(a), abs(b)), abs_tol)

def get_taken_date(file_path):
    taken_date = get_creation_date(file_path)
    if get_file_extension(file_path) in EXIF_PICTURE_EXTENSION_PREFIX:
        try:
            exif_dict = piexif.load(file_path)
            if exif_dict:
                if "Exif" in exif_dict:
                    exif = exif_dict["Exif"]
                    if exif:
                        if piexif.ExifIFD.DateTimeOriginal in exif:
                            date_time_original_binary = exif[piexif.ExifIFD.DateTimeOriginal]
                            if date_time_original_binary:
                                date_time_original = date_time_original_binary.decode("utf-8")
                                taken_date = datetime.datetime.strptime(date_time_original, '%Y:%m:%d %H:%M:%S')
        except:
            pass
    return datetime.datetime.fromtimestamp(taken_date.timestamp())

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

def check_file_taken_vs_creation_date(file_path):
    valid = False
    error_message = None
    taken_date = get_taken_date(file_path)
    creation_date = get_creation_date(file_path)
    if isclose(taken_date.timestamp(), creation_date.timestamp()):
        valid = True
    else:
        valid = False
        error_message = "Taken date (" + str(taken_date) + ") and creation date (" + str(creation_date) + ") not matching"
    return (valid, error_message)
    
def check_file_taken_vs_modification_date(file_path):
    valid = False
    error_message = None
    taken_date = get_taken_date(file_path)
    modification_date = get_modification_date(file_path)
    if isclose(taken_date.timestamp(), modification_date.timestamp()):
        valid = True
    else:
        valid = False
        error_message = "Taken date (" + str(taken_date) + ") and modification date (" + str(modification_date) + ") not matching"
    return (valid, error_message)

def check_file_date(file_path):
    (valid, error_message) = check_file_taken_vs_creation_date(file_path)
    if valid:
        (valid, error_message) = check_file_taken_vs_modification_date(file_path)
    return (valid, error_message)

def is_panorama_folder(folder_name):
    return folder_name == PANORAMA_FOLDER_NAME

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
    if verbose:
        print_file_error_message(file_path, error_message)
    return (valid, error_message)

def check_and_process_files_in_folders(folder_pathes, process, verbose):
    valid = True
    invalid_files = []
    for folder_path in folder_pathes:
        (folder_valid, folder_invalid_files) = check_and_process_files_in_folder(folder_path, process, verbose)
        valid &= folder_valid
        invalid_files += folder_invalid_files
    return (valid, invalid_files)

def main(argv):
    parser = argparse.ArgumentParser()
    parser.add_argument('-d', '--directory', nargs='+', help="Directory", required=True)
    parser.add_argument('-p', '--process', help="Rename files and set dates", default=False, action="store_true")
    parser.add_argument('-v', '--verbose', help="Verbose", default=False, action="store_true")
    args = parser.parse_args(argv)
    (valid, invalid_files) = check_and_process_files_in_folders(args.directory, args.process, args.verbose)
    if not args.verbose and not valid:
        for invalid_file in invalid_files:
            print_file_error_message(invalid_file[0], invalid_file[1])
    return valid

if __name__ == "__main__":
    main(sys.argv[1:])