import file
import re
import os

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
        match = re.match("^" + prefix + "\d\d\d\d\d" + " - " + prefix + "\d\d\d\d\d" + "$", file_name_no_extension)
        if match:
            valid = True
        else:
            valid = False
    else:
        valid = False
    return valid