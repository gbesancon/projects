import file
import datetime
import piexif

EXIF_PICTURE_EXTENSION = [
    ".jpeg",
    ".jpg",
    ".png",
    ".tif",
    ".tiff"
]

def has_exif(file_path):
    has = False
    if file.get_file_extension(file_path) in EXIF_PICTURE_EXTENSION:
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