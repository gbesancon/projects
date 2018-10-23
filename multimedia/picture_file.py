import file
import re
import os
import piexif
import datetime
import file_messages
import multimedia_file

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

def is_picture_file(file_path):
    file_extension = file.get_file_extension(file_path)
    return file_extension in PICTURE_EXTENSION_PREFIX

PANORAMA_FOLDER_NAME = "Panorama"

def is_panorama_folder_name(folder_name):
    return folder_name == PANORAMA_FOLDER_NAME

def is_panorama_folder_path(folder_path):
    folder_name = os.path.basename(folder_path)
    return is_panorama_folder_name(folder_name)
    
def is_valid_panorama_file_name(file_path):
    valid = True
    file_name = file.get_file_name(file_path)
    file_name_no_extension = os.path.splitext(file_name)[0]
    file_extension = file.get_file_extension(file_path)
    if file_extension in PICTURE_EXTENSION_PREFIX:
        prefix = PICTURE_EXTENSION_PREFIX[file_extension]
        match = re.match(r"^" + prefix + r"\d\d\d\d\d" + " - " + prefix + r"\d\d\d\d\d" + r"$", file_name_no_extension)
        if match:
            valid = True
        else:
            valid = False
    else:
        valid = False
    return valid

def has_exif(file_path):
    has = False
    if file.get_file_extension(file_path) in EXIF_PICTURE_EXTENSION_PREFIX:
        has = get_exif(file_path) is not None
    return has

def get_exif(file_path):
    exif_dict = None
    try:
        exif_dict = piexif.load(file_path)
    except:
        pass
    return exif_dict

UTF8 = "utf-8"
EXIF_DATE_FORMAT = r'%Y:%m:%d %H:%M:%S'

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
                        date_time_original = date_time_original_binary.decode(UTF8)
                        exif_date_time_original = datetime.datetime.strptime(date_time_original, EXIF_DATE_FORMAT)
    return exif_date_time_original

def set_exif_date_time_original(file_path, file_date):
    try:
        if has_exif(file_path):
            exif_dict = get_exif(file_path)
            exif_dict["Exif"][piexif.ExifIFD.DateTimeOriginal] = file_date.strftime(EXIF_DATE_FORMAT).encode(UTF8)
            exif_bytes = piexif.dump(exif_dict)
            piexif.insert(exif_bytes, file_path)
    except:
        pass

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
                        date_time_digitized = date_time_digitized_binary.decode(UTF8)
                        exif_date_time_digitized = datetime.datetime.strptime(date_time_digitized, EXIF_DATE_FORMAT)
    return exif_date_time_digitized

def set_exif_date_time_digitized(file_path, file_date):
    try:
        if has_exif(file_path):
            exif_dict = get_exif(file_path)
            exif_dict["Exif"][piexif.ExifIFD.DateTimeDigitized] = file_date.strftime(EXIF_DATE_FORMAT).encode(UTF8)
            exif_bytes = piexif.dump(exif_dict)
            piexif.insert(exif_bytes, file_path)
    except:
        pass

def is_valid_dated_picture_file_name(file_path):
    valid = True
    date = None
    file_name = file.get_file_name(file_path)
    match = re.match(r"^IMG_(\d\d\d\d\d\d\d\d_\d\d\d\d\d\d).*$", file_name)
    if match:
        valid = True
        date = datetime.datetime.strptime(match.group(1), r'%Y%m%d_%H%M%S')
    else:
        valid = False
    return (valid, date)

def get_date_from_picture_file_name(file_path):
    (_, date) = is_valid_dated_picture_file_name(file_path)
    return date

def get_picture_file_date(file_path):
    file_date_valid = False
    file_date = None
    if has_exif_date_time_original(file_path):
        file_date_valid = True
        file_date = get_exif_date_time_original(file_path)
    elif has_exif_date_time_digitized(file_path):
        file_date_valid = True
        file_date = get_exif_date_time_digitized(file_path)
    else:
        (file_date_valid, file_date) = is_valid_dated_picture_file_name(file_path)
        if not file_date_valid:
            file_date = None
    return (file_date_valid, file_date)

def set_picture_file_date(file_path, file_date):
    set_exif_date_time_original(file_path, file_date)
    set_exif_date_time_digitized(file_path, file_date)
    file.set_file_date(file_path, file_date)

def check_picture_file_date(file_path):
    (file_date_valid, file_date) = multimedia_file.get_multimedia_file_date(file_path, get_picture_file_date)
    file_error_message = None
    if file_date_valid:
        if has_exif_date_time_original(file_path):
            if file_date_valid:
                (file_date_valid, file_date, file_error_message) = file.check_file_dates(file_path, "File date", lambda f : file_date, "EXIF Date Time Original", get_exif_date_time_original)
        if has_exif_date_time_digitized(file_path):
            if file_date_valid:
                (file_date_valid, file_date, file_error_message) = file.check_file_dates(file_path, "File date", lambda f : file_date, "EXIF Date Time Digitized", get_exif_date_time_digitized)
        if file_date_valid:
            (file_date_valid, file_date, file_error_message) = multimedia_file.check_multimedia_file_date(file_path, file_date)
    else:
        file_error_message = "No date identified for file"
    return (file_date_valid, file_date, file_error_message)
    
def set_picture_file_dates(folder_path, picture_file_names, process, verbose):
    files_processed = False
    files_process_comments = {}

    for picture_file_name in picture_file_names:
        picture_file_path = os.path.join(folder_path, picture_file_name)
        (file_date_valid, file_date) = multimedia_file.get_multimedia_file_date(picture_file_path, get_picture_file_date)
        if file_date_valid:
            (folder_date_valid, folder_date) = multimedia_file.has_valid_dated_folder_name(picture_file_path)
            if folder_date_valid:
                if file_date.year == folder_date.year and file_date.month == folder_date.month and file_date.day == folder_date.day:
                    if process:
                        set_picture_file_date(picture_file_path, file_date)
                    if not process or verbose:
                        file_messages.add_file_message(files_process_comments, picture_file_path, "Set File date (" + str(file_date) + ")")
                else:
                    file_messages.add_file_message(files_process_comments, picture_file_path, "File date (" + str(file_date) + ") not matching dated folder (" + folder_path + ")")
            else:
                (folder_period_date_valid, folder_period_beginning_date, folder_period_end_date) = multimedia_file.has_valid_period_dated_folder_name(picture_file_path)
                if folder_period_date_valid:
                    if folder_period_beginning_date <= file_date and file_date <= folder_period_end_date:
                        if process:
                            set_picture_file_date(picture_file_path, file_date)
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
    regular_file_names = [f for f in picture_file_names if multimedia_file.is_valid_file_name(os.path.join(folder_path, f), PICTURE_EXTENSION_PREFIX)]
    panorama_file_names = [f for f in picture_file_names if is_valid_panorama_file_name(os.path.join(folder_path, f))]
    other_file_names = [f for f in picture_file_names if f not in regular_file_names and f not in panorama_file_names]
    if not is_panorama_folder_name(os.path.basename(folder_path)):
        if len(panorama_file_names) > 0:
            # Move panorama files to Panorama folder
            panorama_folder_path = os.path.join(folder_path, PANORAMA_FOLDER_NAME)
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
        (file_date_valid, file_date) = multimedia_file.get_multimedia_file_date(picture_file_path, get_picture_file_date)
        if file_date_valid:
            (folder_date_valid, folder_date) = multimedia_file.has_valid_dated_folder_name(picture_file_path)
            if folder_date_valid:
                if not (file_date.year == folder_date.year and file_date.month == folder_date.month and file_date.day == folder_date.day):
                    dated_folder_name = "{:0>4d}-{:0>2d}-{:0>2d} - XXX".format(file_date.year, file_date.month, file_date.day)
                    dated_folder_path = os.path.join(folder_path, dated_folder_name)
                    if process:
                        file.move_file_to_folder(picture_file_path, dated_folder_path)
                    if not process or verbose:
                        file_messages.add_file_message(files_process_comments, picture_file_path, "Move to " + dated_folder_path)
            else:
                (folder_period_date_valid, folder_period_beginning_date, folder_period_end_date) = multimedia_file.has_valid_period_dated_folder_name(picture_file_path)
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