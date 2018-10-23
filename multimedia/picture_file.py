import file
import re
import os
import piexif
import datetime

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
