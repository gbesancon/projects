from __future__ import print_function
import argparse
import os
import platform
import datetime
import sys
import re
import piexif
import multimedia_file

def print_invalid_file(file_path, error_message):
    if error_message:
        print(file_path + ": " + error_message)
    else:
        print(file_path + ": OK")

def check_files_in_folder(folder_path, verbose):
    folder_valid = True
    folder_invalid_files = []
    for path,dirs,files in os.walk(folder_path):
        for dir_name in dirs:
            subfolder_path = os.path.join(path,dir_name)
            (subfolder_valid, subfolder_invalid_files) = check_files_in_folder(subfolder_path, verbose)
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
    return folder_name == "Panorama"

def check_file_name(file_path, verbose):
    valid = False
    error_message = None
    folder_path = os.path.dirname(file_path)
    folder_name = os.path.basename(folder_path)
    file_name = os.path.basename(file_path)
    file_name_no_extension = os.path.splitext(file_name)[0]
    file_extension = os.path.splitext(file_name)[1].lower()
    if file_extension in multimedia_file.extension_prefix:
        prefix = multimedia_file.extension_prefix[file_extension]
        match = re.match("^" + prefix + "\d\d\d\d\d$", file_name_no_extension)
        if match:
            if not is_panorama_folder(folder_name):
                valid = True
            else:
                valid = False
                error_message = "File should not be stored in a Panorama folder"
        else:
            match = re.match("^" + prefix + "\d\d\d\d\d" + " - " + prefix + "\d\d\d\d\d" + "$", file_name_no_extension)
            if match:
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
        error_message = "Extension " + file_extension + " not supported"
    if verbose:
        print_invalid_file(file_path, error_message)
    return (valid, error_message)

def check_files_in_folders(folder_pathes, verbose):
    valid = True
    invalid_files = []
    for folder_path in folder_pathes:
        (folder_valid, folder_invalid_files) = check_files_in_folder(folder_path, verbose)
        valid &= folder_valid
        invalid_files += folder_invalid_files
    return (valid, invalid_files)

def main(argv):
    parser = argparse.ArgumentParser()
    parser.add_argument('-d', '--directory', nargs='+', help="Directory", required=True)
    parser.add_argument('-v', '--verbose', help="Verbose", default=True, action="store_true")
    args = parser.parse_args(argv)
    (valid, invalid_files) = check_files_in_folders(args.directory, args.verbose)
    if not args.verbose and not valid:
        for invalid_file in invalid_files:
            print_invalid_file(invalid_file[0], invalid_file[1])
    return valid

if __name__ == "__main__":
    main(sys.argv[1:])